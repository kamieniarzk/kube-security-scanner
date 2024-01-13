package com.kcs.web;

import com.kcs.score.KubeScoreFacade;
import com.kcs.score.KubeScoreJsonResultDto;
import com.kcs.score.KubeScoreRunRequest;
import com.kcs.score.KubeScoreRunDto;
import com.kcs.workload.WorkloadScanResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kube-score")
@RequiredArgsConstructor
class KubeScoreController {

  private final KubeScoreFacade facade;

  @GetMapping("/runs/{namespace}")
  List<KubeScoreRunDto> getScoresByNamespace(@PathVariable String namespace) {
    return facade.getRunsByNamespace(namespace);
  }

  @PostMapping("/runs")
  KubeScoreRunDto runAndPersistScore(@RequestBody KubeScoreRunRequest runRequest) {
    return facade.score(runRequest);
  }

  @GetMapping(path = "/runs/{id}/result", produces = {"application/json", "text/csv"})
  WorkloadScanResult getRunResult(@PathVariable String id) {
    return facade.getResult(id);
  }
}
