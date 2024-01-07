package com.kcs.scheduling;

import com.kcs.workload.WorkloadScanRunService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RunFactory {
  private final WorkloadScanRunService workloadScanRunService;

  Runnable create(RunType runType) {
    return switch (runType) {
      case WORKLOAD_SCAN -> workloadScanRunService::runAggregatedScan;
      case COMPLIANCE -> throw new UnsupportedOperationException();
    };
  }
}
