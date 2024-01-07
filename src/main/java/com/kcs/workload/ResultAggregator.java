package com.kcs.workload;

public interface ResultAggregator {
  WorkloadScanResult get(String runId);
}
