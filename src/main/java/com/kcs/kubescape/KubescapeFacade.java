package com.kcs.kubescape;

import com.kcs.workload.ResultMapper;
import com.kcs.workload.WorkloadScanResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KubescapeFacade {

  private final KubescapeRunner runner;
  private final KubescapeResultRepository resultRepository;
  private final ResultMapper<KubescapeResult> resultMapper;

  public String run(KubescapeRunRequest runRequest) {
    return runner.run(runRequest);
  }

  public WorkloadScanResult getResult(String runId) {
    var domainResult = KubescapeJsonResultParser.parseFullResult(getRawResult(runId));
    return resultMapper.map(domainResult);
  }

  public String getOriginalResult(String runId) {
    return getRawResult(runId);
  }

  private String getRawResult(String runId) {
    return resultRepository.getAsString(runId.concat(".json"));
  }
}
