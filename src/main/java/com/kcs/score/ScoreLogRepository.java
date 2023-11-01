package com.kcs.score;

import com.kcs.shared.AbstractLogRepository;
import com.kcs.shared.LogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class ScoreLogRepository extends AbstractLogRepository {

  private final String scoreDirectory;

  ScoreLogRepository(LogRepository logRepository, @Value("${filesystem.locations.score:/tmp/kube-config-scanner/score}") String scoreDirectory) {
    super(logRepository);
    this.scoreDirectory = scoreDirectory;
  }

  @Override
  protected String getDirectory() {
    return scoreDirectory;
  }
}
