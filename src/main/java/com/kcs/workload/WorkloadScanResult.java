package com.kcs.workload;

import java.util.List;
import java.util.Map;

public record WorkloadScanResult(Map<String, List<K8sResource>> namespaceResourceMap) {
}
