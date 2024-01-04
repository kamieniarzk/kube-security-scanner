package com.kcs.web;

import com.kcs.aggregated.AggregatedScanResult;
import com.kcs.aggregated.ResultAggregator;
import com.kcs.aggregated.AggregatedScanRunDto;
import com.kcs.aggregated.AggregatedScanRunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workloads")
@RequiredArgsConstructor
class WorkloadScanController {

  private final AggregatedScanRunService scanRunService;
  private final ResultAggregator scanResultService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = "/scans", produces = MediaType.APPLICATION_JSON_VALUE)
  AggregatedScanRunDto runAggregatedScan() {
    return scanRunService.runAggregatedScan();
  }

  @GetMapping(path = "/scans", produces = MediaType.APPLICATION_JSON_VALUE)
  List<AggregatedScanRunDto> getAllAggregatedScanRuns() {
    return scanRunService.getAllScanRuns();
  }

  @GetMapping(path = "/scans/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  AggregatedScanRunDto getAggregatedScanRun(@PathVariable String id) {
    return scanRunService.getAggregatedScanRun(id);
  }

  @GetMapping(path = "/scans/{id}/result", produces = MediaType.APPLICATION_JSON_VALUE)
  AggregatedScanResult getAggregatedScanResult(@PathVariable String id) {
    return scanResultService.get(id);
  }
}
