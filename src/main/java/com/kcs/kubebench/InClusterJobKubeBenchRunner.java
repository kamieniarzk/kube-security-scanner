package com.kcs.kubebench;

import com.kcs.job.JobDto;
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
  public KubeBenchScanDto run(KubeBenchRunRequest runRequest) {
    var clusterType = runRequest.clusterType() == null ? contextHolder.getClusterType() : runRequest.clusterType();
    var clusterName = contextHolder.getClusterName();
    JobDto jobDto;
    if (runRequest.generic()) {
      jobDto = jobService.run(KubeBenchJobDefinition.get(), "kube-bench");
    } else if (runRequest.benchmark() != null) {
      jobDto = jobService.runWithArgs(KubeBenchJobDefinition.getMaster(), "kube-bench", runRequest.benchmark());
    } else {
      jobDto = jobService.runJobFromUrl(JobDefinitionLocator.getJobDefinitionUrl(clusterType, runRequest.controlPlane()), "kube-bench");
    }

    return kubeBenchRepository.save(new KubeBenchScanCreate(jobDto.id(), clusterName, clusterType));
  }
}
