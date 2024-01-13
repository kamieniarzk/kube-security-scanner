package com.kcs.scheduling;

import com.kcs.aggregated.AggregatedScanRequest;
import com.kcs.aggregated.WorkloadScanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RunFactory {
  private final WorkloadScanService workloadScanService;

  Runnable create(AggregatedScanRequest aggregatedScanRequest) {
    return () -> workloadScanService.runAggregatedScan(aggregatedScanRequest);
  }
}
