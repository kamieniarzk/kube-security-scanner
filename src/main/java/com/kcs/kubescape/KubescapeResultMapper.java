package com.kcs.kubescape;

import com.kcs.shared.Check;
import com.kcs.shared.KubernetesResource;
import com.kcs.shared.ScanResult;
import com.kcs.shared.Severity;
import com.kcs.aggregated.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class KubescapeResultMapper implements ResultMapper<KubescapeResult> {

  private static final String ORIGIN = "kubescape";

  private final KubescapeControlDictionary controlDictionary = new KubescapeControlDictionary();

  @Override
  public ScanResult map(KubescapeResult kubescapeResult) {
    var k8sResources = mapInternal(kubescapeResult);

    var nonNamespacedResources = k8sResources.stream().filter(resource -> resource.getNamespace() == null).toList();
    var namespacedResources = k8sResources.stream()
        .filter(resource -> resource.getNamespace() != null)
        .collect(Collectors.groupingBy(KubernetesResource::getNamespace));

    var skippedControls = kubescapeResult.getSummaryDetails().getControls().values().stream()
        .filter(controlSummary -> "skipped".equals(controlSummary.getStatusInfo().getStatus()))
        .map(this::map)
        .toList();

    return new ScanResult(namespacedResources, nonNamespacedResources, skippedControls);
  }

  private Check map(KubescapeResult.SummaryDetails.ControlSummary controlSummary) {
    var controlMetadata = controlDictionary.get(controlSummary.getControlID());
    return new Check(mapSeverity(controlMetadata), controlMetadata.getName(), controlMetadata.getDescription(), controlMetadata.getRemediation(), ORIGIN, controlMetadata.getControlID(), false, true, controlSummary.getStatusInfo().getInfo());
  }

  List<KubernetesResource> mapInternal(KubescapeResult result) {
    var mapOfResources = result.getResources().stream()
        .collect(Collectors.toMap(KubescapeResult.Resource::getResourceID, Function.identity()));
    return result.getResults().stream()
        .map(res -> map(res, mapOfResources))
        .collect(Collectors.toList());
  }

  KubernetesResource map(KubescapeResult.Result result, Map<String, KubescapeResult.Resource> resourcesMap) {
    var kubescapeResource = resourcesMap.get(result.getResourceID());
    return new KubernetesResource(kubescapeResource.getObject().getKind(), kubescapeResource.getObject().getNamespace(), kubescapeResource.getObject().getName(), map(result.getControls()));
  }

  List<Check> map(List<KubescapeResult.Control> controls) {
    return controls.stream().map(this::map).collect(Collectors.toList());
  }

  Check map(KubescapeResult.Control control) {
    var controlMetadata = controlDictionary.get(control.getControlID());
    return new Check(mapSeverity(controlMetadata), controlMetadata.getName(), controlMetadata.getDescription(), controlMetadata.getRemediation(), ORIGIN, controlMetadata.getControlID(), "passed".equals(control.getStatus().getStatus()));
  }

  Severity mapSeverity(KubescapeControls.Control control) {
    return switch (control.getBaseScore()) {
      case 1, 2, 3 -> Severity.LOW;
      case 4, 5, 6 -> Severity.MEDIUM;
      case 7, 8 -> Severity.HIGH;
      default -> Severity.CRITICAL;
    };
  }
}
