package com.kcs.kubescore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class KubeScorer {

  private final KubeScoreScanner runner;
  private final KubeScoreRunRepository scoreRepository;
  private final ScoreResultRepository logRepository;

  public KubeScoreScanDto score(String namespace, String additionalFlags) {
    var score = runner.score(namespace, additionalFlags);
    var scoreRun = scoreRepository.save(new KubeScoreScanDto(namespace));
    logRepository.save(score, scoreRun.id());
    return scoreRun;
  }

  public KubeScoreScanDto scoreAll(String additionalFlags) {
    var score = runner.scoreAllNamespaces(additionalFlags);
    var scoreRun = scoreRepository.save(new KubeScoreScanDto());
    logRepository.save(score, scoreRun.id());
    return scoreRun;
  }
}
