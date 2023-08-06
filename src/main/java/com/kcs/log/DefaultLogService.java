package com.kcs.log;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.kcs.k8s.KubernetesApiClientWrapper;
import com.kcs.shared.ScanRepository;
import com.kcs.shared.ScanRun;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class DefaultLogService implements LogService {

  private final LogRepository logRepository;
  private final KubernetesApiClientWrapper k8sApi;
  private final ScanRepository scanRepository;

  @Override
  public void persistRunLogsForRunsWithoutStoredLogs() {
    scanRepository.getAllWithoutStoredLogs()
        .forEach(this::persistRunLogs);
  }

  @Override
  public void persistRunLogs(ScanRun scanRun) {
    log.info("Persisting missing logs for run: {}", scanRun.id());
    try (var logStream = k8sApi.streamPodLogs(scanRun.podName())) {
      logRepository.save(logStream, scanRun.podName());
    } catch (IOException ioException) {
      log.error("Failed to persist run logs for run: {}", scanRun.id(), ioException);
    }

    scanRepository.updateLogsStored(scanRun.id(), true);
  }
}
