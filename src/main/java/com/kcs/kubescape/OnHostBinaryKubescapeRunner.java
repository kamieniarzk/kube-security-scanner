package com.kcs.kubescape;

import com.kcs.util.ProcessRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

@Slf4j
@Component
class OnHostBinaryKubescapeRunner implements KubescapeRunner {

  private static final String KUBESCAPE_RUN_COMMAND_PATTERN = "kubescape scan %s--format json --format-version v2 %s --output %s %s";
  private static final String KUBESCAPE_SH = "/kubescape.sh";
  private final String resultDirectory;
  private final KubescapeRunRepository runRepository;

  public OnHostBinaryKubescapeRunner(@Value("${filesystem.locations.kubescape:/tmp/kube-security-scanner/kubescape}") String resultDirectory, KubescapeRunRepository runRepository) {
    this.resultDirectory = resultDirectory;
    this.runRepository = runRepository;
  }

  @Override
  public KubescapeScan run(KubescapeScanRequest runRequest) {
    var savedRun = runRepository.save(new KubescapeScan(runRequest.frameworks(), runRequest.namespaces()));
    var command = buildCommand(runRequest, savedRun.getId());
    try {
      overrideScript(command);
      var output = ProcessRunner.runWithExceptionHandling(command);
      log.info("Kubescape run stdout:\n{}", output);
    } catch (RuntimeException | IOException exception) {
      log.error("Failed to run kubescape", exception);
      runRepository.deleteById(savedRun.getId());
      throw new RuntimeException(exception);
    }

    savedRun.setCommand(command);
    runRepository.save(savedRun);

    return savedRun;
  }

  private static void overrideScript(String command) throws IOException {
    var scriptFile = new File(KUBESCAPE_SH);
    Files.write(scriptFile.toPath(), command.getBytes());
  }

  private String buildCommand(KubescapeScanRequest request, String scanId) {
    var frameworksString = request.frameworks() == null || request.frameworks().isEmpty() ? "" : "framework ".concat(buildFrameworksString(request.frameworks()));
    var namespacesString = buildNamespacesString(request.namespaces());
    var scanLocation = Paths.get(resultDirectory, scanId);
    return KUBESCAPE_RUN_COMMAND_PATTERN.formatted(frameworksString, namespacesString, scanLocation, request.additionalFlags() == null ? "" : request.additionalFlags());
  }

  private static String buildNamespacesString(Set<String> namespaces) {
    if (namespaces == null || namespaces.isEmpty()) {
      return "";
    }
    var sb = new StringBuilder();
    sb.append("--include-namespaces ");
    namespaces.forEach(namespace -> sb.append(namespace).append(","));
    sb.delete(sb.length() - 1, sb.length());
    return sb.toString();
  }

  private static String buildFrameworksString(Set<KubescapeFramework> frameworks) {
    var sb = new StringBuilder();
    frameworks.forEach(framework -> sb.append(framework.getName()).append(" "));
    return sb.toString();
  }
}
