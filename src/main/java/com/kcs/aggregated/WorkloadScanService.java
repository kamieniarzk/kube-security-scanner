package com.kcs.aggregated;

import java.util.List;

public interface WorkloadScanService {
  AggregatedScanRun runAggregatedScan(AggregatedScanRequest aggregatedScanRequest);
  AggregatedScanRun getAggregatedScanRun(String id);
  List<AggregatedScanRun> getAllScanRuns();
}
