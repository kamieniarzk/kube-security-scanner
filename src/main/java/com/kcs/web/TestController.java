package com.kcs.web;

import com.kcs.bench.KubeBenchFacade;
import com.kcs.bench.KubeBenchJsonResultDto;
import com.kcs.bench.persistence.KubeBenchRunDto;
import com.kcs.trivy.TrivyFacade;
import com.kcs.trivy.persistence.TrivyRunDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
class TestController {

  private final TrivyFacade trivyFacade;
  private final KubeBenchFacade kubeBenchFacade;


  @GetMapping("/trivy")
  List<TrivyRunDto> getAllTrivyRuns() {
    return trivyFacade.getAll();
  }

  @GetMapping("/bench")
  List<KubeBenchRunDto> getAllBenchJobs() {
    return kubeBenchFacade.getAllRuns();
  }

  @PostMapping("/trivy")
  TrivyRunDto runTrivy() {
    return trivyFacade.run();
  }

  @PostMapping("/bench")
  KubeBenchRunDto runBench(@RequestBody BenchRunRequest runRequest) {
    return kubeBenchFacade.run();
  }

  @GetMapping("/bench/runs/{id}/result")
  KubeBenchJsonResultDto getKubeBenchResult(@PathVariable String id) {
    return kubeBenchFacade.getRunResult(id);
  }

  record BenchRunRequest() {}
}
