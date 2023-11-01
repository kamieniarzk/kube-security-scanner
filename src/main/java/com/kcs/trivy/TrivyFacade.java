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

  public TrivyRunDto run() {
    return runner.run();
  }

  public List<TrivyRunDto> getAll() {
    return repository.getAll();
  }
}
