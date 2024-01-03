package com.kcs.aggregated;

public interface AggregatedScanRunService {
  AggregatedScanRunDto runAggregatedScan();
  AggregatedScanRunDto getAggregatedScanRun(String id);
}
