package com.kcs.web;

import com.kcs.kubebench.KubeBenchFacade;
import com.kcs.kubebench.KubeBenchJsonResultDto;
import com.kcs.kubebench.KubeBenchRunRequest;
import com.kcs.kubebench.persistence.KubeBenchScanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scans/kube-bench")
@RequiredArgsConstructor
class KubeBenchController {

  private final KubeBenchFacade kubeBenchFacade;

  @GetMapping
  List<KubeBenchScanDto> getAllBenchJobs() {
    return kubeBenchFacade.getAllRuns();
  }

  @PostMapping
  KubeBenchScanDto runBench(@RequestBody KubeBenchRunRequest runRequest) {
    return kubeBenchFacade.run(runRequest);
  }

  @GetMapping("/{id}/result")
  KubeBenchJsonResultDto getKubeBenchResult(@PathVariable String id) {
    return kubeBenchFacade.getRunResult(id);
  }

  @GetMapping("/{id}/original-result")
  String getOriginalResult(@PathVariable String id) {
    return kubeBenchFacade.getOriginalResult(id);
  }
}
