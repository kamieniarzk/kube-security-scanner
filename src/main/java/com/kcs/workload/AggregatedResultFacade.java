package com.kcs.workload;

import com.kcs.NoDataFoundException;
import com.kcs.kubescape.KubescapeFacade;
import com.kcs.score.KubeScoreFacade;
import com.kcs.trivy.TrivyFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AggregatedResultFacade {

  private final AggregatedScanRepository runRepository;
  private final KubeScoreFacade scoreFacade;
  private final TrivyFacade trivyFacade;
  private final KubescapeFacade kubescapeFacade;
  private final ResultAggregator aggregator;

  public WorkloadScanResult aggregateResult(String runId) {
    var runDto = runRepository.findById(runId).orElseThrow(NoDataFoundException::new);
    var scoreResult = runDto.getScoreRunId() == null ? WorkloadScanResult.empty() : scoreFacade.getResult(runDto.getScoreRunId());
    var trivyResult = runDto.getTrivyRunId() == null ? WorkloadScanResult.empty() : trivyFacade.getResult(runDto.getTrivyRunId());
    var kubescapeResult = runDto.getKubescapeRunId() == null ? WorkloadScanResult.empty() : kubescapeFacade.getResult(runDto.getKubescapeRunId());
    return aggregator.aggregate(trivyResult, scoreResult, kubescapeResult).setAggregatedScanId(runId);
  }
}
