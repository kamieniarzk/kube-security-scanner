package com.kcs.workload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkloadScanResult {
  private String scanId;
  private Boolean aggregated;
  private Map<String, List<K8sResource>> namespacedResources;
  private List<K8sResource> nonNamespacedResources;
  private List<Check> skippedChecks;

  public WorkloadScanResult(Map<String, List<K8sResource>> namespacedResources) {
    this(null, null, namespacedResources, Collections.emptyList(), Collections.emptyList());
  }

  public WorkloadScanResult(Map<String, List<K8sResource>> namespacedResources, List<K8sResource> nonNamespacedResources, List<Check> skippedChecks) {
    this.namespacedResources = namespacedResources;
    this.nonNamespacedResources = nonNamespacedResources;
    this.skippedChecks = skippedChecks;
  }

  public static WorkloadScanResult empty() {
    return new WorkloadScanResult(null, null, Collections.emptyMap(), Collections.emptyList(), Collections.emptyList());
  }

  public WorkloadScanResult setScanId(String scanId) {
    this.scanId = scanId;
    this.aggregated = false;
    return this;
  }

  public WorkloadScanResult setAggregatedScanId(String scanId) {
    this.scanId = scanId;
    this.aggregated = true;
    return this;
  }
}
