package com.kcs.kubescape;

import com.kcs.shared.AbstractLogRepository;
import com.kcs.shared.LogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class KubescapeResultRepository extends AbstractLogRepository {

  private final String scoreDirectory;

  KubescapeResultRepository(LogRepository logRepository, @Value("${filesystem.locations.kubescape:/tmp/kube-security-scanner/kubescape}") String resultDirectory) {
    super(logRepository);
    this.scoreDirectory = resultDirectory;
  }

  @Override
  protected String getDirectory() {
    return scoreDirectory;
  }
}
