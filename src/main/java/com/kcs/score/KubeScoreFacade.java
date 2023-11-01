package com.kcs.score;

import com.kcs.score.persistence.document.KubeScoreRepository;
import com.kcs.score.persistence.document.KubeScoreRunCreate;
import com.kcs.score.persistence.document.KubeScoreRunDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KubeScoreFacade {

  private final KubeScoreRunner runner;
  private final KubeScoreRepository scoreRepository;
  private final ScoreLogRepository logRepository;


  @Transactional
  public String score(String namespace) {
    var score = runner.score(namespace);
    var scoreId = scoreRepository.save(new KubeScoreRunCreate(namespace));
    logRepository.save(score, scoreId);
    return scoreId;
  }

  public List<KubeScoreRunDto> getRunsByNamespace(String namespace) {
    return scoreRepository.getByNamespace(namespace);
  }

  public KubeScoreResultDto getResult(String runId) {
    var logs = logRepository.getAsString(runId);
    return KubeScoreResultParser.parse(logs);
  }
}
