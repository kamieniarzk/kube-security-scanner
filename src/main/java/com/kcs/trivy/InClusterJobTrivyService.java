package com.kcs.trivy;

import com.kcs.trivy.persistence.TrivyRepository;
import com.kcs.trivy.persistence.TrivyRunCreate;
import com.kcs.trivy.persistence.TrivyRunDto;
import com.kcs.job.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class InClusterJobTrivyService implements TrivyService {

  private final TrivyRepository repository;
  private final JobService jobService;

  @Override
  public TrivyRunDto run() {
    var jobRunDto = jobService.runJobFromUrlDefinitionWithContextServiceAccount(TrivyJobDefinition.get(), "trivy-scan-job");
    return repository.save(new TrivyRunCreate(jobRunDto.id()));
  }

  @Override
  public List<TrivyRunDto> getAll() {
    return repository.getAll();
  }
}
