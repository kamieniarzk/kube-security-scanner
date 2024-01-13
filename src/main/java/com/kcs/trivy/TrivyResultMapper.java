package com.kcs.trivy;

import com.kcs.shared.Check;
import com.kcs.shared.KubernetesResource;
import com.kcs.shared.ScanResult;
import com.kcs.shared.Severity;
import com.kcs.aggregated.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
class TrivyResultMapper implements ResultMapper<TrivyResult> {

  private static final String ORIGIN = "trivy";

  @Override
  public ScanResult map(TrivyResult trivyResult) {
    var resources = trivyResult.getResources().stream()
        .map(TrivyResultMapper::map)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    var resourcesGrouped = resources.stream()
        .collect(Collectors.groupingBy(KubernetesResource::getNamespace));

    return new ScanResult(resourcesGrouped);
  }

  static Optional<KubernetesResource> map(TrivyResult.Resource trivyResource) {
    var vulnerabilities = map(trivyResource.getResults());
    if (vulnerabilities.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new KubernetesResource(trivyResource.getKind(), trivyResource.getNamespace(), trivyResource.getName(), vulnerabilities));
  }

  static List<Check> map(List<TrivyResult.Result> trivyResults) {
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

  static Check map(TrivyResult.Misconfiguration trivyMisconfiguration) {
    return new Check(
        Severity.valueOf(trivyMisconfiguration.getSeverity()),
        trivyMisconfiguration.getTitle(),
        trivyMisconfiguration.getDescription(),
        trivyMisconfiguration.getResolution(),
        ORIGIN, trivyMisconfiguration.getId());
  }
}
