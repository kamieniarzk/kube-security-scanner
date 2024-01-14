package com.kcs.web;

import com.kcs.shared.ResultSearchParams;
import com.kcs.shared.ScanResult;
import com.kcs.aggregated.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scans/aggregated")
@RequiredArgsConstructor
class AggregatedScanController {

  private final WorkloadScanService scanRunService;
  private final AggregatedResultFacade resultFacade;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
  AggregatedScanRun runAggregatedScan(@RequestBody AggregatedScanRequest aggregatedScanRequest) {
    return scanRunService.runAggregatedScan(aggregatedScanRequest);
  }

  @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
  List<AggregatedScanRun> getAllAggregatedScanRuns() {
    return scanRunService.getAllScanRuns();
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  AggregatedScanRun getAggregatedScanRun(@PathVariable String id) {
    return scanRunService.getAggregatedScanRun(id);
  }

  @GetMapping(path = "/{id}/result", produces = {"application/json", "text/csv"})
  ScanResult getAggregatedScanResult(@PathVariable String id, ResultSearchParams searchParams) {
    return resultFacade.aggregateResult(id, searchParams);
  }
}
