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

  private static final String MASTER_JOB_URL = "https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job-master.yaml";
  private static final String WORKER_JOB_URL = "https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml";

  private final JobService jobService;
  private final KubeBenchRepository kubeBenchRepository;

  @Override
  public KubeBenchRunDto run(Boolean master) {
    var jobDefinitionUrl = master ? MASTER_JOB_URL : WORKER_JOB_URL;
    var targetArg = "master node etcd policies";
    var jobDto = jobService.runJobFromUrlDefinitionWithModifiedCommand(jobDefinitionUrl, "kube-bench", "kube-bench run --json --targets".concat(" ").concat(targetArg));
    return kubeBenchRepository.save(new KubeBenchRunCreate(jobDto.id(), master));
  }

  @Override
  public List<KubeBenchRunDto> getAll() {
    return kubeBenchRepository.getAll();
  }
}
