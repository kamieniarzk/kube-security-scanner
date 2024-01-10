package com.kcs.workload;

import com.kcs.NoDataFoundException;
import com.kcs.kubescape.KubescapeFacade;
import com.kcs.score.KubeScoreFacade;
import com.kcs.score.KubeScoreRunRequest;
import com.kcs.trivy.TrivyFacade;
import com.kcs.trivy.TrivyRunRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
class DefaultWorkloadScanRunService implements WorkloadScanRunService {

  private final KubeScoreFacade scoreFacade;
  private final TrivyFacade trivyFacade;
  private final AggregatedScanRepository repository;
  private final KubescapeFacade kubescapeFacade;

  @Override
  public AggregatedScanRun runAggregatedScan(AggregatedRunRequest aggregatedRunRequest) {
    return repository.save(
        AggregatedScanRun.builder()
            .scoreRunId(getKubeScoreRunId(aggregatedRunRequest))
            .trivyRunId(getTrivyRunId(aggregatedRunRequest))
            .kubescapeRunId(getKubescapeRunId(aggregatedRunRequest))
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
  private String getKubeScoreRunId(AggregatedRunRequest aggregatedRunRequest) {
    if (aggregatedRunRequest.kubeScoreRunRequest() != null) {
      return scoreFacade.score(aggregatedRunRequest.kubeScoreRunRequest()).id();
    }
    return null;
  }

  @Nullable
  private String getTrivyRunId(AggregatedRunRequest aggregatedRunRequest) {
    if (aggregatedRunRequest.trivyRunRequest() != null) {
      return trivyFacade.run(aggregatedRunRequest.trivyRunRequest()).getId();
    }
    return null;
  }

  @Nullable
  private String getKubescapeRunId(AggregatedRunRequest aggregatedRunRequest) {
    if (aggregatedRunRequest.kubescapeRunRequest() != null) {
      return kubescapeFacade.run(aggregatedRunRequest.kubescapeRunRequest()).getId();
    }
    return null;
  }
}
