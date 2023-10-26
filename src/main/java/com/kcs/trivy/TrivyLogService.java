package com.kcs.trivy;

import com.kcs.trivy.persistence.TrivyRepository;
import com.kcs.trivy.persistence.TrivyRunDto;
import com.kcs.job.JobRunRepository;
import com.kcs.k8s.KubernetesApiClientWrapper;
import com.kcs.shared.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
class TrivyLogService {

  private final TrivyRepository repository;
  private final JobRunRepository jobRunRepository;
  private final KubernetesApiClientWrapper k8sApi;
  private final LogRepository logRepository;
  private final String logsDirectory;

  TrivyLogService(TrivyRepository trivyRepository, JobRunRepository jobRunRepository,
                  KubernetesApiClientWrapper k8sApi, LogRepository logRepository,
                  @Value("${filesystem.locations.trivy:/tmp/kube-config-scanner/trivy}") String logsDirectory) {
    this.repository = trivyRepository;
    this.jobRunRepository = jobRunRepository;
    this.k8sApi = k8sApi;
    this.logRepository = logRepository;
    this.logsDirectory = logsDirectory;
  }

  @Transactional
  public void persistLogs() {
    repository.getAllWithoutStoredLogs()
        .forEach(this::persistRunLogs);
  }

  public String getLogs(String podName) {
    return logRepository.getAsString(logsDirectory, podName);
  }

  private void persistRunLogs(TrivyRunDto runDto) {
    var jobRunDto = jobRunRepository.get(runDto.jobRunId());
    if (jobRunDto.podName() == null) {
      log.warn("trivy run {} has null podName. Can not store logs", runDto.id());
      return;
    }
    log.info("Persisting missing logs for run: {}", runDto.id());
    try (var logStream = k8sApi.streamPodLogs(jobRunDto.podName())) {
      logRepository.save(logStream, logsDirectory, jobRunDto.podName());
    } catch (IOException ioException) {
      log.error("Failed to persist run logs for run: {}", runDto.id(), ioException);
    }
    repository.updateLogsStored(runDto.id(), true);
  }
}
