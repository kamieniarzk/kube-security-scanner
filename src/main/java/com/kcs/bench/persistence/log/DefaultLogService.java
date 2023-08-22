package com.kcs.bench.persistence.log;

import com.kcs.bench.persistence.dto.KubeBenchRepository;
import com.kcs.bench.persistence.dto.KubeBenchRun;
import com.kcs.k8s.KubernetesApiClientWrapper;
import com.kcs.shared.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
class DefaultLogService implements LogService {

  private final LogRepository logRepository;
  private final KubernetesApiClientWrapper k8sApi;
  private final KubeBenchRepository kubeBenchRepository;
  private final String logsDirectory;

  DefaultLogService(LogRepository logRepository, KubernetesApiClientWrapper k8sApi,
                    KubeBenchRepository kubeBenchRepository,
                    @Value("${filesystem.log-location:}") String logsDirectory) {
    this.logRepository = logRepository;
    this.k8sApi = k8sApi;
    this.kubeBenchRepository = kubeBenchRepository;
    this.logsDirectory = logsDirectory;
  }

  @Override
  public void persistRunLogsForRunsWithoutStoredLogs() {
    kubeBenchRepository.getAllWithoutStoredLogs()
        .forEach(this::persistRunLogs);
  }

  @Override
  public void persistRunLogs(KubeBenchRun kubeBenchRun) {
    log.info("Persisting missing logs for run: {}", kubeBenchRun.id());
    try (var logStream = k8sApi.streamPodLogs(kubeBenchRun.podName())) {
      logRepository.save(logStream, logsDirectory, kubeBenchRun.podName());
    } catch (IOException ioException) {
      log.error("Failed to persist run logs for run: {}", kubeBenchRun.id(), ioException);
    }

    kubeBenchRepository.updateLogsStored(kubeBenchRun.id(), true);
  }
}
