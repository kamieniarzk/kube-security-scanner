package com.kcs.score;

import com.kcs.score.persistence.document.KubeScoreRepository;
import com.kcs.score.persistence.document.KubeScoreRunCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class KubeScorer {

  private final KubeScoreRunner runner;
  private final KubeScoreRepository scoreRepository;
  private final ScoreResultRepository logRepository;

  public String score(String namespace) {
    var score = runner.score(namespace);
    var scoreId = scoreRepository.save(new KubeScoreRunCreate(namespace));
    logRepository.save(score, scoreId);
    return scoreId;
  }

  public String scoreAll() {
    var score = runner.scoreAllNamespaces();
    var scoreId = scoreRepository.save(new KubeScoreRunCreate(false));
    logRepository.save(score, scoreId);
    return scoreId;
  }
}
