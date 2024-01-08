package com.kcs.kubescape;

import com.kcs.workload.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class KubescapeResultMapper implements ResultMapper<KubescapeResult> {

  private static final String ORIGIN_FORMAT = "kubescape %s";

  private final KubescapeControlDictionary controlDictionary = new KubescapeControlDictionary();

  @Override
  public WorkloadScanResult map(KubescapeResult kubescapeResult) {
    var k8sResources = mapInternal(kubescapeResult);

    var nonNamespacedResources = k8sResources.stream().filter(resource -> resource.getNamespace() == null).toList();
    var namespacedResources = k8sResources.stream()
        .filter(resource -> resource.getNamespace() != null)
        .collect(Collectors.groupingBy(K8sResource::getNamespace));
    return new WorkloadScanResult(namespacedResources, nonNamespacedResources);
  }

  List<K8sResource> mapInternal(KubescapeResult result) {
    var mapOfResources = result.getResources().stream()
        .collect(Collectors.toMap(KubescapeResult.Resource::getResourceID, Function.identity()));
    return result.getResults().stream()
        .map(res -> map(res, mapOfResources))
        .collect(Collectors.toList());
  }

  K8sResource map(KubescapeResult.Result result, Map<String, KubescapeResult.Resource> resourcesMap) {
    var kubescapeResource = resourcesMap.get(result.getResourceID());
    return new K8sResource(kubescapeResource.getObject().getKind(), kubescapeResource.getObject().getNamespace(), kubescapeResource.getObject().getName(), map(result.getControls()));
  }

  List<Vulnerability> map(List<KubescapeResult.Control> controls) {
    return controls.stream().map(this::map).collect(Collectors.toList());
  }

  Vulnerability map(KubescapeResult.Control control) {
    var controlMetadata =  controlDictionary.get(control.getControlID());
    return new Vulnerability(mapSeverity(controlMetadata), controlMetadata.getName(), controlMetadata.getDescription(), controlMetadata.getRemediation(), ORIGIN_FORMAT.formatted(controlMetadata.getControlID()));
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
