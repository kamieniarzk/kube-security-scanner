package com.kcs.workload;

public interface ResultAggregator {
  WorkloadScanResult aggregateResult(String runId);
}
