package com.kcs.score;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class KubeScorer {

  private final KubeScoreRunner runner;
  private final KubeScoreRunRepository scoreRepository;
  private final ScoreResultRepository logRepository;

  public String score(String namespace, String additionalFlags) {
    var score = runner.score(namespace, additionalFlags);
    var scoreId = scoreRepository.save(new KubeScoreRunDto(namespace)).id();
    logRepository.save(score, scoreId);
    return scoreId;
  }

  public String scoreAll(String additionalFlags) {
    var score = runner.scoreAllNamespaces(additionalFlags);
    var scoreId = scoreRepository.save(new KubeScoreRunDto()).id();
    logRepository.save(score, scoreId);
    return scoreId;
  }
}
