package com.kcs.workload;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record WorkloadScanResult(Map<String, List<K8sResource>> namespacedResources, List<K8sResource> nonNamespacedResources) {
  public WorkloadScanResult(Map<String, List<K8sResource>> namespacedResources) {
    this(namespacedResources, Collections.emptyList());
  }
}
