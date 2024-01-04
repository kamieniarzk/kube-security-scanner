package com.kcs.aggregated;

import com.kcs.aggregated.persistence.AggregatedRunRepository;
import com.kcs.score.KubeScoreFacade;
import com.kcs.score.KubeScoreRunRequest;
import com.kcs.trivy.TrivyFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class DefaultAggregatedScanRunService implements AggregatedScanRunService {

  private final KubeScoreFacade scoreFacade;
  private final TrivyFacade trivyFacade;
  private final AggregatedRunRepository repository;

  @Override
  public AggregatedScanRunDto runAggregatedScan() {
    var scoreRunId = scoreFacade.score(new KubeScoreRunRequest(false, null));
    var trivyRunId = trivyFacade.run().id();
    return repository.save(scoreRunId, trivyRunId);
  }

  @Override
  public AggregatedScanRunDto getAggregatedScanRun(String id) {
    return repository.get(id);
  }

  @Override
  public List<AggregatedScanRunDto> getAllScanRuns() {
    return repository.getAll();
  }
}
