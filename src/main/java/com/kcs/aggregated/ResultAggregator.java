package com.kcs.aggregated;

public interface ResultAggregator {
  AggregatedScanResult get(String runId);
}
