package com.kcs.web;

import com.kcs.workload.WorkloadScanResult;
import com.kcs.workload.AggregatedScanRun;
import com.kcs.workload.WorkloadScanRunService;
import com.kcs.workload.ResultAggregator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workloads")
@RequiredArgsConstructor
class WorkloadScanController {

  private final WorkloadScanRunService scanRunService;
  private final ResultAggregator scanResultService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = "/scans", produces = MediaType.APPLICATION_JSON_VALUE)
  AggregatedScanRun runAggregatedScan() {
    return scanRunService.runAggregatedScan();
  }

  @GetMapping(path = "/scans", produces = MediaType.APPLICATION_JSON_VALUE)
  List<AggregatedScanRun> getAllAggregatedScanRuns() {
    return scanRunService.getAllScanRuns();
  }

  @GetMapping(path = "/scans/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  AggregatedScanRun getAggregatedScanRun(@PathVariable String id) {
    return scanRunService.getAggregatedScanRun(id);
  }

  @GetMapping(path = "/scans/{id}/result", produces = MediaType.APPLICATION_JSON_VALUE)
  WorkloadScanResult getAggregatedScanResult(@PathVariable String id) {
    return scanResultService.get(id);
  }
}
