package com.kcs.aggregated;

import java.util.List;

public interface AggregatedScanRunService {
  AggregatedScanRunDto runAggregatedScan();
  AggregatedScanRunDto getAggregatedScanRun(String id);
  List<AggregatedScanRunDto> getAllScanRuns();
}
