package com.kcs.aggregated;

import com.kcs.shared.NoDataFoundException;
import com.kcs.kubescape.KubescapeFacade;
import com.kcs.kubescore.KubeScoreFacade;
import com.kcs.trivy.TrivyFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
class DefaultWorkloadScanService implements WorkloadScanService {

  private final KubeScoreFacade scoreFacade;
  private final TrivyFacade trivyFacade;
  private final AggregatedScanRepository repository;
  private final KubescapeFacade kubescapeFacade;

  @Override
  public AggregatedScanRun runAggregatedScan(AggregatedScanRequest aggregatedScanRequest) {
    return repository.save(
        AggregatedScanRun.builder()
            .scoreRunId(getKubeScoreRunId(aggregatedScanRequest))
            .trivyRunId(getTrivyRunId(aggregatedScanRequest))
            .kubescapeRunId(getKubescapeRunId(aggregatedScanRequest))
            .date(LocalDateTime.now())
            .build());
  }
  @Override
  public AggregatedScanRun getAggregatedScanRun(String id) {
    return repository.findById(id).orElseThrow(NoDataFoundException::new);
  }

  @Override
  public List<AggregatedScanRun> getAllScanRuns() {
    return repository.findAll();
  }

  @Nullable
  private String getKubeScoreRunId(AggregatedScanRequest aggregatedScanRequest) {
    if (aggregatedScanRequest.kubeScoreScanRequest() != null) {
      return scoreFacade.score(aggregatedScanRequest.kubeScoreScanRequest()).id();
    }
    return null;
  }

  @Nullable
  private String getTrivyRunId(AggregatedScanRequest aggregatedScanRequest) {
    if (aggregatedScanRequest.trivyRunRequest() != null) {
      return trivyFacade.run(aggregatedScanRequest.trivyRunRequest()).getId();
    }
    return null;
  }

  @Nullable
  private String getKubescapeRunId(AggregatedScanRequest aggregatedScanRequest) {
    if (aggregatedScanRequest.kubescapeScanRequest() != null) {
      return kubescapeFacade.run(aggregatedScanRequest.kubescapeScanRequest()).getId();
    }
    return null;
  }
}
