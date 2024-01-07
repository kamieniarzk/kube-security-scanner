package com.kcs.workload;

import java.util.List;

public interface WorkloadScanRunService {
  AggregatedScanRunDto runAggregatedScan();
  AggregatedScanRunDto getAggregatedScanRun(String id);
  List<AggregatedScanRunDto> getAllScanRuns();
}
