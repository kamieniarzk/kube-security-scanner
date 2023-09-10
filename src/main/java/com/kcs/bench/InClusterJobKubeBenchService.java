package com.kcs.bench;

import com.kcs.bench.persistence.KubeBenchRepository;
import com.kcs.bench.persistence.KubeBenchRunCreate;
import com.kcs.bench.persistence.KubeBenchRunDto;
import com.kcs.job.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InClusterJobKubeBenchService implements KubeBenchService {

  private final JobService jobService;
  private final KubeBenchRepository kubeBenchRepository;

  @Override
  public KubeBenchRunDto run() {
    var jobDto = jobService.runJobFromUrlDefinitionWithModifiedCommand("https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml", "kube-bench", "kube-bench --json");
    return kubeBenchRepository.save(new KubeBenchRunCreate(jobDto.id()));
  }

  @Override
  public List<KubeBenchRunDto> getAll() {
    return kubeBenchRepository.getAll();
  }
}
