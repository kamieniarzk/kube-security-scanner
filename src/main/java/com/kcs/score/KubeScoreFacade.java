package com.kcs.score;

import com.kcs.workload.ResultMapper;
import com.kcs.workload.WorkloadScanResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KubeScoreFacade {

  private final KubeScorer scorer;
  private final KubeScoreRunRepository scoreRepository;
  private final ScoreResultRepository logRepository;
  private final ResultMapper<List<KubeScoreJsonResultDto>> resultMapper;

  public KubeScoreRunDto score(KubeScoreRunRequest runRequest) {
    if (runRequest.namespace() == null) {
      return scorer.scoreAll(runRequest.additionalFlags());
    }

    return scorer.score(runRequest.namespace(), runRequest.additionalFlags());
  }

  public List<KubeScoreRunDto> getRunsByNamespace(String namespace) {
    return scoreRepository.findByNamespace(namespace);
  }

  public WorkloadScanResult getResult(String runId) {
    var logs = logRepository.getAsString(runId);
    var rawResult = KubeScoreJsonResourceParser.parseFullResult(logs);
    return resultMapper.map(rawResult);
  }

  public String getOriginalResult(String runId) {
    return logRepository.getAsString(runId);
  }
}
