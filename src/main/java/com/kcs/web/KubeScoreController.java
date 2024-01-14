package com.kcs.web;

import com.kcs.kubescore.KubeScoreFacade;
import com.kcs.kubescore.KubeScoreScanDto;
import com.kcs.kubescore.KubeScoreScanRequest;
import com.kcs.shared.ResultSearchParams;
import com.kcs.shared.ScanResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scans/kube-score")
@RequiredArgsConstructor
class KubeScoreController {

  private final KubeScoreFacade facade;

  @GetMapping("/namespaces/{namespace}")
  List<KubeScoreScanDto> getScoresByNamespace(@PathVariable String namespace) {
    return facade.getRunsByNamespace(namespace);
  }

  @PostMapping
  KubeScoreScanDto runAndPersistScore(@RequestBody KubeScoreScanRequest runRequest) {
    return facade.score(runRequest);
  }

  @GetMapping(path = "/{id}/result", produces = {"application/json", "text/csv"})
  ScanResult getRunResult(@PathVariable String id, ResultSearchParams searchParams) {
    return facade.getResult(id, searchParams);
  }
}
