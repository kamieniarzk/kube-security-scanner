package com.kcs.web;

import com.kcs.kubescape.KubescapeFacade;
import com.kcs.kubescape.KubescapeResult;
import com.kcs.kubescape.KubescapeRunRequest;
import com.kcs.workload.ResultMapper;
import com.kcs.workload.WorkloadScanResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kubescape")
@RequiredArgsConstructor
class KubescapeController {

  private final KubescapeFacade facade;
  private final ResultMapper<KubescapeResult> resultResultMapper;

  @PostMapping("/runs")
  String runKubescape(@RequestBody KubescapeRunRequest runRequest) {
    return facade.run(runRequest);
  }

  @GetMapping("/runs/{id}/result")
  WorkloadScanResult getWorkloadResult(@PathVariable String id) {
    return facade.getResult(id);
  }

  @GetMapping("/runs/{id}/original-result")
  String getFullScanResult(@PathVariable String id) {
    return facade.getOriginalResult(id);
  }
}
