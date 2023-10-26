package com.kcs.web;

import com.kcs.bench.KubeBenchJsonResultDto;
import com.kcs.bench.KubeBenchResultService;
import com.kcs.bench.KubeBenchService;
import com.kcs.bench.persistence.KubeBenchRunDto;
import com.kcs.trivy.TrivyService;
import com.kcs.trivy.persistence.TrivyRunDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
class TestController {

  private final KubeBenchService kubeBenchService;
  private final TrivyService trivyService;
  private final KubeBenchResultService kubeBenchResultService;


  @GetMapping("/trivy")
  List<TrivyRunDto> getAllTrivyRuns() {
    return trivyService.getAll();
  }

  @GetMapping("/bench")
  List<KubeBenchRunDto> getAllBenchJobs() {
    return kubeBenchService.getAll();
  }

  @PostMapping("/trivy")
  TrivyRunDto runTrivy() {
    return trivyService.run();
  }

  @PostMapping("/bench")
  KubeBenchRunDto runBench(@RequestBody BenchRunRequest runRequest) {
    return kubeBenchService.run();
  }

  @GetMapping("/bench/runs/{id}/result")
  KubeBenchJsonResultDto getKubeBenchResult(@PathVariable String id) {
    return kubeBenchResultService.getResult(id);
  }

  record BenchRunRequest() {}
}
