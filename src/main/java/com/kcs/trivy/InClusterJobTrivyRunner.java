package com.kcs.trivy;

import com.kcs.job.JobDto;
import com.kcs.job.JobService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class InClusterJobTrivyRunner implements TrivyRunner {

  private static final String COMMAND_PATTERN = "trivy k8s --format json --timeout 3600s --all-namespaces -q %s %s %s %s all";
  private final JobService jobService;
  private final TrivyScanRepository repository;

  @Override
  public TrivyScanDto run(TrivyRunRequest runRequest) {
    var command = buildCommand(runRequest);
    var jobRunDto = jobService.runJobFromUrlDefinitionWithContextServiceAccount(getJobDefinitionWithCommand(command), "trivy-scan-job");
    return saveTrivyRun(runRequest, jobRunDto, command);
  }

  @NotNull
  private TrivyScanDto saveTrivyRun(TrivyRunRequest runRequest, JobDto jobDto, String command) {
    var trivyRun = TrivyScanDto.builder()
        .jobRunId(jobDto.id())
        .command(command)
        .scanners(runRequest.scanners())
        .severity(runRequest.severityFilter())
        .compliance(runRequest.compliance())
        .build();
    return repository.save(trivyRun);
  }

  private static String getJobDefinitionWithCommand(String command) {
    return TrivyJobDefinition.get().replace("${TRIVY_COMMAND}", command);
  }

  private String buildCommand(TrivyRunRequest runRequest) {

    String severity = "";
    if (runRequest.severityFilter() != null && !runRequest.severityFilter().isEmpty()) {
      severity = "--severity ".concat(setToString(runRequest.severityFilter().stream().map(Enum::name).collect(Collectors.toSet())));
    }

    String scanners = "";
    if (runRequest.scanners() != null && !runRequest.scanners().isEmpty()) {
      scanners = "--scanners ".concat(setToString(runRequest.scanners().stream().map(TrivyScanner::getName).collect(Collectors.toSet())));
    }

    String additionalFlags = runRequest.additionalFlags() == null ? "" : runRequest.additionalFlags();
    String compliance = runRequest.compliance() == null ? "" : "--compliance=".concat(runRequest.compliance().getName());
    return COMMAND_PATTERN.formatted(severity, scanners, compliance, additionalFlags);
  }

  private static String setToString(Set<String> setOfStrings) {
    var sb = new StringBuilder();
    sb.append('"');
    setOfStrings.forEach(string -> sb.append(string).append(","));
    sb.delete(sb.length() - 1, sb.length());
    sb.append('"');
    return sb.toString();
  }
}
