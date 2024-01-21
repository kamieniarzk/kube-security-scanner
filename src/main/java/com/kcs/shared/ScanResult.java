package com.kcs.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScanResult {
  private String scanId;
  private Boolean aggregated;
  private Map<String, List<KubernetesResource>> namespacedResources;
  private List<KubernetesResource> nonNamespacedResources;
  private List<Check> skippedChecks;

  public ScanResult(Map<String, List<KubernetesResource>> namespacedResources) {
    this(null, null, namespacedResources, Collections.emptyList(), Collections.emptyList());
  }

  public ScanResult(Map<String, List<KubernetesResource>> namespacedResources, List<KubernetesResource> nonNamespacedResources, List<Check> skippedChecks) {
    this.namespacedResources = namespacedResources;
    this.nonNamespacedResources = nonNamespacedResources;
    this.skippedChecks = skippedChecks;
  }

  public static ScanResult empty() {
    return new ScanResult(null, null, Collections.emptyMap(), Collections.emptyList(), Collections.emptyList());
  }

  public ScanResult setScanId(String scanId) {
    this.scanId = scanId;
    this.aggregated = false;
    return this;
  }

  @JsonIgnore
  public ScanResult setAggregatedScanId(String scanId) {
    this.scanId = scanId;
    this.aggregated = true;
    return this;
  }
}
