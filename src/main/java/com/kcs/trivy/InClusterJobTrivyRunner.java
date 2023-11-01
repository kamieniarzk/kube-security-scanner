package com.kcs.trivy;

import com.kcs.job.JobService;
import com.kcs.trivy.persistence.TrivyRepository;
import com.kcs.trivy.persistence.TrivyRunCreate;
import com.kcs.trivy.persistence.TrivyRunDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class InClusterJobTrivyRunner implements TrivyRunner {

  private final JobService jobService;
  private final TrivyRepository repository;

  @Override
  public TrivyRunDto run() {
    var jobRunDto = jobService.runJobFromUrlDefinitionWithContextServiceAccount(TrivyJobDefinition.get(), "trivy-scan-job");
    return repository.save(new TrivyRunCreate(jobRunDto.id()));
  }
}
