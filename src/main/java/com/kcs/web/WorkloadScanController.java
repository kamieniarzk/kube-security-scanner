package com.kcs.web;

import com.kcs.workload.*;
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
  private final AggregatedResultFacade resultFacade;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = "/scans", produces = MediaType.APPLICATION_JSON_VALUE)
  AggregatedScanRun runAggregatedScan(@RequestBody AggregatedRunRequest aggregatedRunRequest) {
    return scanRunService.runAggregatedScan(aggregatedRunRequest);
  }

  @GetMapping(path = "/scans", produces = MediaType.APPLICATION_JSON_VALUE)
  List<AggregatedScanRun> getAllAggregatedScanRuns() {
    return scanRunService.getAllScanRuns();
  }

  @GetMapping(path = "/scans/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  AggregatedScanRun getAggregatedScanRun(@PathVariable String id) {
    return scanRunService.getAggregatedScanRun(id);
  }

  @GetMapping(path = "/scans/{id}/result", produces = {"application/json", "text/csv"})
  WorkloadScanResult getAggregatedScanResult(@PathVariable String id) {
    return resultFacade.aggregateResult(id);
  }
}
