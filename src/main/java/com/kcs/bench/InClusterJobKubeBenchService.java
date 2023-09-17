package com.kcs.bench;

import com.kcs.bench.dto.KubeBenchTarget;
import com.kcs.bench.persistence.KubeBenchRepository;
import com.kcs.bench.persistence.KubeBenchRunCreate;
import com.kcs.bench.dto.KubeBenchRunDto;
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
  public KubeBenchRunDto run(KubeBenchTarget target) {
    var jobDto = jobService.runJobFromUrlDefinitionWithModifiedCommand(MASTER_JOB_URL, "kube-bench", "kube-bench run --json --targets".concat(" ").concat(
        target.name()).toLowerCase());
    return kubeBenchRepository.save(new KubeBenchRunCreate(jobDto.id(), target));
  }

  @Override
  public List<KubeBenchRunDto> getAll() {
    return kubeBenchRepository.getAll();
  }
}
