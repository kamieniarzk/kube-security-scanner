package com.kcs.aggregated;

import com.kcs.shared.*;
import com.kcs.kubescape.KubescapeFacade;
import com.kcs.kubescore.KubeScoreFacade;
import com.kcs.trivy.TrivyFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AggregatedResultFacade {

  private final AggregatedScanRepository runRepository;
  private final KubeScoreFacade scoreFacade;
  private final TrivyFacade trivyFacade;
  private final KubescapeFacade kubescapeFacade;
  private final ResultAggregator aggregator;

  public ScanResult aggregateResult(String runId, ResultSearchParams searchParams) {
    var runDto = runRepository.findById(runId).orElseThrow(NoDataFoundException::new);
    var scoreResult = runDto.getScoreRunId() == null ? ScanResult.empty() : scoreFacade.getResult(runDto.getScoreRunId(), searchParams);
    var trivyResult = runDto.getTrivyRunId() == null ? ScanResult.empty() : trivyFacade.getResult(runDto.getTrivyRunId(), searchParams);
    var kubescapeResult = runDto.getKubescapeRunId() == null ? ScanResult.empty() : kubescapeFacade.getResult(runDto.getKubescapeRunId(), searchParams);
    return aggregator.aggregate(trivyResult, scoreResult, kubescapeResult).setAggregatedScanId(runId);
  }

  public List<CheckSummary> getChecksAggregated(String runId, ResultSearchParams searchParams) {
    var aggregatedResult = aggregateResult(runId, searchParams);
    return CheckAggregator.aggregateChecks(aggregatedResult);
  }

  public List<CheckSummary> getChecksAggregatedCompacted(String runId, ResultSearchParams searchParams) {
    var aggregatedResult = aggregateResult(runId, searchParams);
    return CheckAggregator.getCheckSummaryCompacted(aggregatedResult);
  }
}
