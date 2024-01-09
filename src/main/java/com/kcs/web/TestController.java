package com.kcs.web;

import com.kcs.bench.KubeBenchFacade;
import com.kcs.bench.KubeBenchJsonResultDto;
import com.kcs.bench.persistence.KubeBenchRunDto;
import com.kcs.trivy.TrivyFacade;
import com.kcs.trivy.TrivyRunDto;
import com.kcs.trivy.TrivyRunRequest;
import com.kcs.workload.WorkloadScanResult;
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

  @GetMapping("/trivy/result")
  WorkloadScanResult getResult(String id) {
    return trivyFacade.getResult(id);
  }

  @GetMapping("/bench")
  List<KubeBenchRunDto> getAllBenchJobs() {
    return kubeBenchFacade.getAllRuns();
  }

  @PostMapping("/trivy")
  TrivyRunDto runTrivy(@RequestBody TrivyRunRequest runRequest) {
    return trivyFacade.run(runRequest);
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
