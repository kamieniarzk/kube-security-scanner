package com.kcs.workload;

public interface ResultMapper<Source> {
  WorkloadScanResult map(Source source);
}
