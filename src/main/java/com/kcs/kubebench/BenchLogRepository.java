package com.kcs.kubebench;

import com.kcs.shared.AbstractLogRepository;
import com.kcs.shared.LogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class BenchLogRepository extends AbstractLogRepository {

  private final String directory;

  BenchLogRepository(LogRepository logRepository, @Value("${filesystem.locations.bench:/tmp/kube-security-scanner/bench}") String logsDirectory) {
    super(logRepository);
    this.directory = logsDirectory;
  }

  @Override
  protected String getDirectory() {
    return directory;
  }
}
