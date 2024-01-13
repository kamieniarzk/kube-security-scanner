package com.kcs.aggregated;

import com.kcs.shared.NoDataFoundException;
import com.kcs.kubescape.KubescapeFacade;
import com.kcs.kubescore.KubeScoreFacade;
import com.kcs.shared.ScanResult;
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

  public ScanResult aggregateResult(String runId) {
    var runDto = runRepository.findById(runId).orElseThrow(NoDataFoundException::new);
    var scoreResult = runDto.getScoreRunId() == null ? ScanResult.empty() : scoreFacade.getResult(runDto.getScoreRunId());
    var trivyResult = runDto.getTrivyRunId() == null ? ScanResult.empty() : trivyFacade.getResult(runDto.getTrivyRunId());
    var kubescapeResult = runDto.getKubescapeRunId() == null ? ScanResult.empty() : kubescapeFacade.getResult(runDto.getKubescapeRunId());
    return aggregator.aggregate(trivyResult, scoreResult, kubescapeResult).setAggregatedScanId(runId);
  }
}
