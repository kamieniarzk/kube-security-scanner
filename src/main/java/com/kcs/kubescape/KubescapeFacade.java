package com.kcs.kubescape;

import com.kcs.shared.*;
import com.kcs.aggregated.ResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KubescapeFacade {

  private final KubescapeRunner runner;
  private final KubescapeResultRepository resultRepository;
  private final ResultMapper<KubescapeResult> resultMapper;
  private final KubescapeRunRepository runRepository;

  public KubescapeScan run(KubescapeScanRequest runRequest) {
    return runner.run(runRequest);
  }

  public List<KubescapeScan> getAllRuns() {
    return runRepository.findAll();
  }

  public KubescapeScan getRun(String id) {
    return runRepository.findById(id).orElseThrow(NoDataFoundException::new);
  }

  public ScanResult getResult(String runId, ResultSearchParams searchParams) {
    var domainResult = KubescapeJsonResultParser.parseFailedResults(getRawResult(runId));
    var mappedResult = resultMapper.map(domainResult).setScanId(runId);
    return ScanResultFilter
        .withParams(searchParams)
        .filter(mappedResult);
  }

  public KubescapeResult getFullDomainResult(String runId) {
    return KubescapeJsonResultParser.parseAllResults(getRawResult(runId));
  }

  public String getOriginalResult(String runId) {
    return getRawResult(runId);
  }

  private String getRawResult(String runId) {
    return resultRepository.getAsString(runId.concat(".json"));
  }
}
