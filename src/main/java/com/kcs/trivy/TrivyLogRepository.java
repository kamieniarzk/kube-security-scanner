package com.kcs.trivy;

import com.kcs.shared.AbstractLogRepository;
import com.kcs.shared.LogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class TrivyLogRepository extends AbstractLogRepository {

  private final String directory;

  public TrivyLogRepository(LogRepository logRepository, @Value("${filesystem.locations.trivy:/tmp/kube-config-scanner/trivy}") String logsDirectory) {
    super(logRepository);
    this.directory = logsDirectory;
  }

  @Override
  protected String getDirectory() {
    return directory;
  }
}
