package com.kcs.workload;

import java.util.List;

public interface WorkloadScanRunService {
  AggregatedScanRun runAggregatedScan(AggregatedRunRequest aggregatedRunRequest);
  AggregatedScanRun getAggregatedScanRun(String id);
  List<AggregatedScanRun> getAllScanRuns();
}
