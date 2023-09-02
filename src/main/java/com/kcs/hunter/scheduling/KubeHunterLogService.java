package com.kcs.hunter.scheduling;

import com.kcs.hunter.persistence.KubeHunterRepository;
import com.kcs.hunter.persistence.KubeHunterRunDto;
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
class KubeHunterLogService {

  private final KubeHunterRepository repository;
  private final JobRunRepository jobRunRepository;
  private final KubernetesApiClientWrapper k8sApi;
  private final LogRepository logRepository;
  private final String logsDirectory;

  KubeHunterLogService(KubeHunterRepository hunterRepository, JobRunRepository jobRunRepository,
                       KubernetesApiClientWrapper k8sApi, LogRepository logRepository,
                       @Value("${filesystem.locations.hunter:/tmp/kube-config-scanner/hunter}") String logsDirectory) {
    this.repository = hunterRepository;
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

  private void persistRunLogs(KubeHunterRunDto runDto) {
    var jobRunDto = jobRunRepository.get(runDto.jobRunId());
    if (jobRunDto.podName() == null) {
      log.warn("kube-hunter run {} has null podName. Can not store logs", runDto.id());
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
