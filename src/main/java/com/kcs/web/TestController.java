package com.kcs.web;

import com.kcs.bench.KubeBenchService;
import com.kcs.bench.persistence.KubeBenchRunDto;
import com.kcs.hunter.KubeHunterService;
import com.kcs.hunter.persistence.KubeHunterRunDto;
import com.kcs.job.JobRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
class TestController {

  private final KubeBenchService kubeBenchService;
  private final KubeHunterService kubeHunterService;
  private final JobRunRepository jobRunRepository;

  @GetMapping("/hunter")
  List<KubeHunterRunDto> getAllKubeHunterRuns() {
    return kubeHunterService.getAll();
  }

  @GetMapping("/bench")
  List<KubeBenchRunDto> getAllBenchJobs() {
    return kubeBenchService.getAll();
  }

  @PostMapping("/hunter")
  KubeHunterRunDto runHunter(@RequestBody HunterRunRequest runRequest) {
    return kubeHunterService.run(runRequest.args);
  }

  @PostMapping("/bench")
  KubeBenchRunDto runBench(@RequestBody BenchRunRequest runRequest) {
    return kubeBenchService.run();
  }

  record HunterRunRequest(String args) {}
  record BenchRunRequest() {}
}
