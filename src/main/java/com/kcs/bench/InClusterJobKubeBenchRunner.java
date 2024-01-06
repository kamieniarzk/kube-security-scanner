package com.kcs.bench;

import com.kcs.bench.persistence.KubeBenchRepository;
import com.kcs.bench.persistence.KubeBenchRunCreate;
import com.kcs.bench.persistence.KubeBenchRunDto;
import com.kcs.context.ContextHolder;
import com.kcs.job.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class InClusterJobKubeBenchRunner implements KubeBenchRunner {

  private final JobService jobService;
  private final KubeBenchRepository kubeBenchRepository;
  private final ContextHolder contextHolder;

  @Override
  public KubeBenchRunDto run() {
    var clusterType = contextHolder.getClusterType();
    var clusterName = contextHolder.getClusterName();
    var jobDto = jobService.runJobFromUrlDefinitionWithModifiedCommand(JobDefinitionLocator.getJobDefinitionUrl(clusterType), "kube-bench", "kube-bench --json");
    return kubeBenchRepository.save(new KubeBenchRunCreate(jobDto.id(), clusterName, clusterType));
  }
}
