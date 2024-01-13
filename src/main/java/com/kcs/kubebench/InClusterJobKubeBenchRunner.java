package com.kcs.kubebench;

import com.kcs.kubebench.persistence.KubeBenchRepository;
import com.kcs.kubebench.persistence.KubeBenchScanCreate;
import com.kcs.kubebench.persistence.KubeBenchScanDto;
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
  public KubeBenchScanDto run() {
    var clusterType = contextHolder.getClusterType();
    var clusterName = contextHolder.getClusterName();
    var jobDto = jobService.runJobFromUrlDefinitionWithModifiedCommand(JobDefinitionLocator.getJobDefinitionUrl(clusterType), "kube-bench", "kube-bench --json");
    return kubeBenchRepository.save(new KubeBenchScanCreate(jobDto.id(), clusterName, clusterType));
  }
}
