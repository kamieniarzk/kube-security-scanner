package com.kcs.trivy;

import com.kcs.shared.Check;
import com.kcs.shared.KubernetesResource;
import com.kcs.shared.ScanResult;
import com.kcs.shared.Severity;
import com.kcs.aggregated.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    var checks = map(trivyResource.getResults());
    if (checks.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new KubernetesResource(trivyResource.getKind(), trivyResource.getNamespace(), trivyResource.getName(), checks));
  }

  static List<Check> map(List<TrivyResult.Result> trivyResults) {
    if (trivyResults == null) {
      return Collections.emptyList();
    }

    var allMisconfigurations = trivyResults.stream().map(TrivyResult.Result::getMisconfigurations).filter(Objects::nonNull).flatMap(Collection::stream).map(TrivyResultMapper::map);
    var allVulnerabilities = trivyResults.stream().map(TrivyResult.Result::getVulnerabilities).filter(Objects::nonNull).flatMap(Collection::stream).map(TrivyResultMapper::map);
    return Stream.concat(allVulnerabilities, allMisconfigurations).toList();
  }

  static Check map(TrivyResult.Vulnerability trivyVulnerability) {
    return new Check(Severity.valueOf(trivyVulnerability.getSeverity()),
        trivyVulnerability.getTitle(),
        trivyVulnerability.getPkgName(),
        null,
        ORIGIN, trivyVulnerability.vulnerabilityId);
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
