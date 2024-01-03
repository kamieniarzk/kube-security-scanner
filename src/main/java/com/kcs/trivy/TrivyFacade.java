package com.kcs.trivy;

import com.kcs.trivy.persistence.TrivyRepository;
import com.kcs.trivy.persistence.TrivyRunDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrivyFacade {

  private final TrivyRunner runner;
  private final TrivyRepository repository;
  private final TrivyLogRepository logRepository;


  public TrivyRunDto run() {
    return runner.run();
  }

  public List<TrivyRunDto> getAll() {
    return repository.getAll();
  }

  public TrivyFullResultDto getResult(String runId) {
    var logs = logRepository.getAsString(runId);
    return TrivyResultParser.parseFullResult(logs);
  }
}
