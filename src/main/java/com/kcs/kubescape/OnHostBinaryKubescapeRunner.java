package com.kcs.kubescape;

import com.kcs.util.ProcessRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Set;

@Slf4j
@Component
class OnHostBinaryKubescapeRunner implements KubescapeRunner {

  private static final String KUBESCAPE_RUN_COMMAND_PATTERN = "kubescape scan %s--format json --format-version v2 --output %s %s";
  private final String resultDirectory;
  private final KubescapeRunRepository runRepository;

  public OnHostBinaryKubescapeRunner(@Value("${filesystem.locations.kubescape:/tmp/kube-config-scanner/kubescape}") String resultDirectory, KubescapeRunRepository runRepository) {
    this.resultDirectory = resultDirectory;
    this.runRepository = runRepository;
  }

  @Override
  public KubescapeScan run(KubescapeScanRequest runRequest) {
    var savedRun = runRepository.save(new KubescapeScan(runRequest.frameworks()));
    var command = buildCommand(runRequest, savedRun.getId());
    try {
      var output = ProcessRunner.runWithExceptionHandling(command);
    } catch (RuntimeException runtimeException) {
      log.error("Failed to run kubescape", runtimeException);
      runRepository.deleteById(savedRun.getId());
      throw runtimeException;
    }

    savedRun.setCommand(command);
    runRepository.save(savedRun);

    return savedRun;
  }

  private String buildCommand(KubescapeScanRequest request, String scanId) {
    var frameworksString = request.frameworks() == null || request.frameworks().isEmpty() ? "" : "framework ".concat(buildFrameworksString(request.frameworks()));
    var scanLocation = Paths.get(resultDirectory, scanId);
    return KUBESCAPE_RUN_COMMAND_PATTERN.formatted(frameworksString, scanLocation, request.additionalFlags() == null ? "" : request.additionalFlags());
  }

  private static String buildFrameworksString(Set<KubescapeFramework> frameworks) {
    var sb = new StringBuilder();
    frameworks.forEach(framework -> sb.append(framework.getName()).append(" "));
    return sb.toString();
  }
}
