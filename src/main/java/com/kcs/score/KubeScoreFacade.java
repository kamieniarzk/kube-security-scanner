package com.kcs.score;

import com.kcs.score.persistence.document.KubeScoreRepository;
import com.kcs.score.persistence.document.KubeScoreRunDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KubeScoreFacade {

  private final KubeScorer scorer;
  private final KubeScoreRepository scoreRepository;
  private final ScoreResultRepository logRepository;

  public String score(KubeScoreRunRequest runRequest) {
    if (runRequest.namespaced() != null && runRequest.namespaced() && runRequest.namespace() != null) {
      throw new IllegalArgumentException();
    }

    if (runRequest.namespaced() != null && !runRequest.namespaced()) {
      return scorer.scoreAll();
    }

    return scorer.score(runRequest.namespace());
  }

  public List<KubeScoreRunDto> getRunsByNamespace(String namespace) {
    return scoreRepository.getByNamespace(namespace);
  }

  public List<KubeScoreJsonResultDto> getResult(String runId) {
    var logs = logRepository.getAsString(runId);
    return KubeScoreJsonResourceParser.parseFullResult(logs);
  }
}
