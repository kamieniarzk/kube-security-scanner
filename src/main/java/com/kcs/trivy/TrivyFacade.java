package com.kcs.trivy;

import com.kcs.shared.NoDataFoundException;
import com.kcs.aggregated.ResultMapper;
import com.kcs.shared.ResultSearchParams;
import com.kcs.shared.ScanResult;
import com.kcs.shared.ScanResultFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrivyFacade {

  private final TrivyRunner runner;
  private final TrivyScanRepository repository;
  private final TrivyLogRepository logRepository;
  private final ResultMapper<TrivyResult> resultMapper;


  public TrivyScanDto run(TrivyRunRequest runRequest) {
    return runner.run(runRequest);
  }

  public List<TrivyScanDto> getAll() {
    return repository.findAll();
  }

  public ScanResult getResult(String runId, ResultSearchParams searchParams) {
    var runDto = repository.findById(runId).orElseThrow(NoDataFoundException::new);
    if (runDto.getCompliance() != null) {
      throw new UnsupportedOperationException("Not possible to aggregate trivy compliance scans, as they do not show result namespace");
    }
    var logs = logRepository.getAsString(runId);
    var rawResult = TrivyResultParser.parseFullResult(logs);
    var mappedResult = resultMapper.map(rawResult).setScanId(runId);
    return ScanResultFilter
        .withParams(searchParams)
        .filter(mappedResult);
  }

  public String getOriginalResult(String runId) {
    return logRepository.getAsString(runId);
  }
}
