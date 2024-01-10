package com.kcs.trivy;

import com.kcs.workload.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
class TrivyResultMapper implements ResultMapper<TrivyFullResultDto> {

  private static final String ORIGIN_FORMAT = "trivy-%s";

  @Override
  public WorkloadScanResult map(TrivyFullResultDto trivyFullResultDto) {
    var resources = trivyFullResultDto.getResources().stream()
        .map(TrivyResultMapper::map)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    var resourcesGrouped = resources.stream()
        .collect(Collectors.groupingBy(K8sResource::getNamespace));

    return new WorkloadScanResult(resourcesGrouped);
  }

  static Optional<K8sResource> map(TrivyFullResultDto.Resource trivyResource) {
    var vulnerabilities = map(trivyResource.getResults());
    if (vulnerabilities.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new K8sResource(trivyResource.getKind(), trivyResource.getNamespace(), trivyResource.getName(), vulnerabilities));
  }

  static List<Vulnerability> map(List<TrivyFullResultDto.Result> trivyResults) {
    if (trivyResults == null) {
      return Collections.emptyList();
    }

    var filteredResults = trivyResults.stream()
        .filter(result -> result.getMisconfigurations() != null && result.getMisconfSummary() != null)
        .collect(Collectors.toCollection(ArrayList::new));

    if (filteredResults.isEmpty()) {
      return Collections.emptyList();
    }

    if (filteredResults.size() > 1) {
      log.warn("Found more than one result but only taking one into account");
    }

    var result = filteredResults.get(0);

    if (result.getMisconfigurations() == null) {
      return Collections.emptyList();
    }

    return result.getMisconfigurations().stream()
        .map(TrivyResultMapper::map)
        .toList();
  }

  static Vulnerability map(TrivyFullResultDto.Misconfiguration trivyMisconfiguration) {
    return new Vulnerability(
        Severity.valueOf(trivyMisconfiguration.getSeverity()),
        trivyMisconfiguration.getTitle(),
        trivyMisconfiguration.getDescription(),
        trivyMisconfiguration.getResolution(),
        ORIGIN_FORMAT.formatted(trivyMisconfiguration.getId()));
  }
}
