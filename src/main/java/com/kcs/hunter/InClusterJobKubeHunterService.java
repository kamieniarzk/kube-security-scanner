package com.kcs.hunter;

import com.kcs.hunter.persistence.KubeHunterRepository;
import com.kcs.hunter.persistence.KubeHunterRunCreate;
import com.kcs.hunter.persistence.KubeHunterRunDto;
import com.kcs.job.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class InClusterJobKubeHunterService implements KubeHunterService {

  private final KubeHunterRepository repository;
  private final JobService jobService;

  @Override
  public KubeHunterRunDto run(String args) {
    var jobRunDto = jobService.runJobFromUrlDefinitionWithContainerArgs("https://raw.githubusercontent.com/aquasecurity/kube-hunter/main/job.yaml", "kube-hunter", args);
    return repository.save(new KubeHunterRunCreate(args, jobRunDto.id()));
  }

  @Override
  public List<KubeHunterRunDto> getAll() {
    return repository.getAll();
  }
}
