package com.kcs.trivy;

import com.kcs.workload.ResultMapper;
import com.kcs.workload.WorkloadScanResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrivyFacade {

  private final TrivyRunner runner;
  private final TrivyRunRepository repository;
  private final TrivyLogRepository logRepository;
  private final ResultMapper<TrivyFullResultDto> resultMapper;


  public TrivyRunDto run(TrivyRunRequest runRequest) {
    return runner.run(runRequest);
  }

  public List<TrivyRunDto> getAll() {
    return repository.findAll();
  }

  public WorkloadScanResult getResult(String runId) {
    var logs = logRepository.getAsString(runId);
    var rawResult = TrivyResultParser.parseFullResult(logs);
    return resultMapper.map(rawResult);
  }

  public String getOriginalResult(String runId) {
    return logRepository.getAsString(runId);
  }
}
