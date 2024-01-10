package com.kcs.score;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class KubeScorer {

  private final KubeScoreRunner runner;
  private final KubeScoreRunRepository scoreRepository;
  private final ScoreResultRepository logRepository;

  public KubeScoreRunDto score(String namespace, String additionalFlags) {
    var score = runner.score(namespace, additionalFlags);
    var scoreRun = scoreRepository.save(new KubeScoreRunDto(namespace));
    logRepository.save(score, scoreRun.id());
    return scoreRun;
  }

  public KubeScoreRunDto scoreAll(String additionalFlags) {
    var score = runner.scoreAllNamespaces(additionalFlags);
    var scoreRun = scoreRepository.save(new KubeScoreRunDto());
    logRepository.save(score, scoreRun.id());
    return scoreRun;
  }
}
