package com.kcs.web;

import com.kcs.score.KubeScoreService;
import com.kcs.score.persistence.document.KubeScoreRun;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kube-score")
@RequiredArgsConstructor
class KubeScoreController {

  private final KubeScoreService scoreService;

  @GetMapping("/{namespace}")
  List<KubeScoreRun> getScoresByNamespace(@PathVariable String namespace) {
    return scoreService.getByNamespace(namespace);
  }

  @PostMapping
  String runAndPersistScore(@RequestBody CreateRun createRun) {
    return scoreService.score(createRun.namespace);
  }

  record CreateRun(String namespace) { }
}
