package com.kcs.bench;

import com.kcs.bench.persistence.KubeBenchRepository;
import com.kcs.bench.persistence.KubeBenchRunDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KubeBenchFacade {

  private final KubeBenchRunner runner;
  private final KubeBenchRepository benchRepository;
  private final BenchLogRepository logRepository;

  public KubeBenchRunDto run() {
    return runner.run();
  }

  public List<KubeBenchRunDto> getAllRuns() {
    return benchRepository.getAll();
  }

  public KubeBenchJsonResultDto getRunResult(String id) {
    return KubeBenchJsonResultParser.parse(logRepository.getAsString(id));
  }
}
