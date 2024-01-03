package com.kcs.aggregated;

import java.util.List;
import java.util.Map;

public record AggregatedScanResult(Map<String, List<K8sResource>> namespaceResourceMap) {
}
