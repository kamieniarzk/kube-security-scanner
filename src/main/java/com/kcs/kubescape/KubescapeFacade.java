package com.kcs.kubescape;

import com.kcs.shared.NoDataFoundException;
import com.kcs.aggregated.ResultMapper;
import com.kcs.shared.ScanResult;
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

  public ScanResult getResult(String runId) {
    var domainResult = KubescapeJsonResultParser.parseFailedResults(getRawResult(runId));
    return resultMapper.map(domainResult).setScanId(runId);
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
