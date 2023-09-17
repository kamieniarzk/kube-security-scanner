package com.kcs.bench;

import com.kcs.bench.persistence.KubeBenchRepository;
import com.kcs.bench.dto.KubeBenchRunDto;
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
class KubeBenchLogService {

  private final KubeBenchRepository benchRepository;
  private final JobRunRepository jobRunRepository;
  private final KubernetesApiClientWrapper k8sApi;
  private final LogRepository logRepository;
  private final String logsDirectory;

  KubeBenchLogService(KubeBenchRepository benchRepository, JobRunRepository jobRunRepository,
                      KubernetesApiClientWrapper k8sApi, LogRepository logRepository,
                      @Value("${filesystem.locations.bench:/tmp/kube-config-scanner/bench}") String logsDirectory) {
    this.benchRepository = benchRepository;
    this.jobRunRepository = jobRunRepository;
    this.k8sApi = k8sApi;
    this.logRepository = logRepository;
    this.logsDirectory = logsDirectory;
  }

  public String getLogs(String podName) {
    return logRepository.getAsString(logsDirectory, podName);
  }

  @Transactional
  public void persistLogs() {
    benchRepository.getAllWithoutStoredLogs()
        .forEach(this::persistRunLogs);
  }

  private void persistRunLogs(KubeBenchRunDto benchRunDto) {
    var jobRunDto = jobRunRepository.get(benchRunDto.jobRunId());
    if (jobRunDto.podName() == null) {
      log.warn("kube-bench run {} has null podName. Can not store logs", benchRunDto.id());
      return;
    }
    log.info("Persisting missing logs for run: {}", benchRunDto.id());
    try (var logStream = k8sApi.streamPodLogs(jobRunDto.podName())) {
      logRepository.save(logStream, logsDirectory, jobRunDto.podName());
    } catch (IOException ioException) {
      log.error("Failed to persist run logs for run: {}", benchRunDto.id(), ioException);
    }
    benchRepository.updateLogsStored(benchRunDto.id(), true);
  }
}
