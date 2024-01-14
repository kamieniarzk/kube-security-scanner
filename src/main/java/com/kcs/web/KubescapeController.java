package com.kcs.web;

import com.kcs.kubescape.KubescapeFacade;
import com.kcs.kubescape.KubescapeScan;
import com.kcs.kubescape.KubescapeScanRequest;
import com.kcs.shared.ResultSearchParams;
import com.kcs.shared.ScanResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kubescape")
@RequiredArgsConstructor
class KubescapeController {

  private final KubescapeFacade facade;

  @PostMapping("/runs")
  KubescapeScan runKubescape(@RequestBody KubescapeScanRequest runRequest) {
    return facade.run(runRequest);
  }

  @GetMapping("/runs")
  List<KubescapeScan> getAllRuns() {
    return facade.getAllRuns();
  }

  @GetMapping("/runs/{id}")
  KubescapeScan getRun(@PathVariable String id) {
    return facade.getRun(id);
  }

  @GetMapping(path = "/runs/{id}/result", produces = {"application/json", "text/csv"})
  ScanResult getWorkloadResult(@PathVariable String id, ResultSearchParams searchParams) {
    return facade.getResult(id, searchParams);
  }

  @GetMapping("/runs/{id}/original-result")
  String getFullScanResult(@PathVariable String id) {
    return facade.getOriginalResult(id);
  }
}
