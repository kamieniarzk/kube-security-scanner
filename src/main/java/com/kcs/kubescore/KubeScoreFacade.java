package com.kcs.kubescore;

import com.kcs.aggregated.ResultMapper;
import com.kcs.shared.ScanResult;
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

  public KubeScoreScanDto score(KubeScoreScanRequest runRequest) {
    if (runRequest.namespace() == null) {
      return scorer.scoreAll(runRequest.additionalFlags());
    }

    return scorer.score(runRequest.namespace(), runRequest.additionalFlags());
  }

  public List<KubeScoreScanDto> getRunsByNamespace(String namespace) {
    return scoreRepository.findByNamespace(namespace);
  }

  public ScanResult getResult(String runId) {
    var logs = logRepository.getAsString(runId);
    var rawResult = KubeScoreJsonResultParser.parseFullResult(logs);
    return resultMapper.map(rawResult).setScanId(runId);
  }

  public String getOriginalResult(String runId) {
    return logRepository.getAsString(runId);
  }
}
