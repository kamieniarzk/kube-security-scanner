package com.kcs.workload;

public interface ResultAggregator {
  WorkloadScanResult aggregate(WorkloadScanResult... results);
}
