package com.kcs.web;

import com.kcs.score.KubeScoreFacade;
import com.kcs.score.KubeScoreResultDto;
import com.kcs.score.persistence.document.KubeScoreRunDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kube-score")
@RequiredArgsConstructor
class KubeScoreController {

  private final KubeScoreFacade scoreFacade;

  @GetMapping("/runs/{namespace}")
  List<KubeScoreRunDto> getScoresByNamespace(@PathVariable String namespace) {
    return scoreFacade.getRunsByNamespace(namespace);
  }

  @PostMapping("/runs")
  String runAndPersistScore(@RequestBody CreateRun createRun) {
    return scoreFacade.score(createRun.namespace);
  }

  @GetMapping("/runs/{id}/result")
  KubeScoreResultDto getRunResult(@PathVariable String id) {
    return scoreFacade.getResult(id);
  }

  record CreateRun(String namespace) { }
}
