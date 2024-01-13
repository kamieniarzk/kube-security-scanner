package com.kcs.web;

import com.kcs.kubescape.KubescapeFacade;
import com.kcs.kubescape.KubescapeRun;
import com.kcs.kubescape.KubescapeRunRequest;
import com.kcs.workload.WorkloadScanResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kubescape")
@RequiredArgsConstructor
class KubescapeController {

  private final KubescapeFacade facade;

  @PostMapping("/runs")
  KubescapeRun runKubescape(@RequestBody KubescapeRunRequest runRequest) {
    return facade.run(runRequest);
  }

  @GetMapping("/runs")
  List<KubescapeRun> getAllRuns() {
    return facade.getAllRuns();
  }

  @GetMapping("/runs/{id}")
  KubescapeRun getRun(@PathVariable String id) {
    return facade.getRun(id);
  }

  @GetMapping(path = "/runs/{id}/result", produces = {"application/json", "text/csv"})
  WorkloadScanResult getWorkloadResult(@PathVariable String id) {
    return facade.getResult(id);
  }

  @GetMapping("/runs/{id}/original-result")
  String getFullScanResult(@PathVariable String id) {
    return facade.getOriginalResult(id);
  }
}
