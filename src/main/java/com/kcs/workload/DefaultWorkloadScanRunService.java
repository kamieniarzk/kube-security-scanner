package com.kcs.workload;

import com.kcs.NoDataFoundException;
import com.kcs.score.KubeScoreFacade;
import com.kcs.score.KubeScoreRunRequest;
import com.kcs.trivy.TrivyFacade;
import com.kcs.trivy.TrivyRunRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class DefaultWorkloadScanRunService implements WorkloadScanRunService {

  private final KubeScoreFacade scoreFacade;
  private final TrivyFacade trivyFacade;
  private final AggregatedScanRepository repository;

  @Override
  public AggregatedScanRun runAggregatedScan() {
    var scoreRunId = scoreFacade.score(new KubeScoreRunRequest(null, null));
    var trivyRunId = trivyFacade.run(new TrivyRunRequest()).getId();
    return repository.save(
        AggregatedScanRun.builder()
            .scoreRunId(scoreRunId)
            .trivyRunId(trivyRunId)
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
}
