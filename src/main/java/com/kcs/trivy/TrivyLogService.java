package com.kcs.trivy;

import com.kcs.job.JobRunRepository;
import com.kcs.k8s.KubernetesApiClientWrapper;
import com.kcs.trivy.persistence.TrivyRepository;
import com.kcs.trivy.persistence.TrivyRunDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
class TrivyLogService {

  private final TrivyRepository repository;
  private final JobRunRepository jobRunRepository;
  private final KubernetesApiClientWrapper k8sApi;
  private final TrivyLogRepository logRepository;

  @Transactional
  public void persistLogs() {
    repository.getAllWithoutStoredLogs()
        .forEach(this::persistRunLogs);
  }

  private void persistRunLogs(TrivyRunDto runDto) {
    var jobRunDto = jobRunRepository.get(runDto.jobRunId());
    if (jobRunDto.podName() == null) {
      log.warn("trivy run {} has null podName. Can not store logs", runDto.id());
      return;
    }
    log.info("Persisting missing logs for run: {}", runDto.id());
    try (var logStream = k8sApi.streamPodLogs(jobRunDto.podName())) {
      logRepository.save(logStream, runDto.id());
    } catch (IOException ioException) {
      log.error("Failed to persist run logs for run: {}", runDto.id(), ioException);
    }
    repository.updateLogsStored(runDto.id(), true);
  }
}
