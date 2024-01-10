package com.kcs.scheduling;

import com.kcs.workload.AggregatedRunRequest;
import com.kcs.workload.WorkloadScanRunService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RunFactory {
  private final WorkloadScanRunService workloadScanRunService;

  Runnable create(AggregatedRunRequest aggregatedRunRequest) {
    return () -> workloadScanRunService.runAggregatedScan(aggregatedRunRequest);
  }
}
