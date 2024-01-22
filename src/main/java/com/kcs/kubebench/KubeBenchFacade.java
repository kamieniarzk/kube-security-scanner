package com.kcs.kubebench;

import com.kcs.kubebench.persistence.KubeBenchRepository;
import com.kcs.kubebench.persistence.KubeBenchScanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KubeBenchFacade {

  private final KubeBenchRunner runner;
  private final KubeBenchRepository benchRepository;
  private final BenchLogRepository logRepository;

  public KubeBenchScanDto run() {
    return runner.run();
  }

  public List<KubeBenchScanDto> getAllRuns() {
    return benchRepository.getAll();
  }

  public KubeBenchJsonResultDto getRunResult(String id) {
    return KubeBenchJsonResultParser.parse(logRepository.getAsString(id));
  }

  public String getOriginalResult(String id) {
    return logRepository.getAsString(id);
  }
}
