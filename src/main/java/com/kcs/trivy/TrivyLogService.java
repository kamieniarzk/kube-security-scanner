package com.kcs.trivy;

import com.kcs.job.JobRunRepository;
import com.kcs.k8s.KubernetesApiClientWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
class TrivyLogService {

  private final TrivyRunRepository repository;
  private final JobRunRepository jobRunRepository;
  private final KubernetesApiClientWrapper k8sApi;
  private final TrivyLogRepository logRepository;

  public void persistLogs() {
    repository.findWhereLogsStoredNullOrFalse()
        .forEach(this::persistRunLogs);
  }

  private void persistRunLogs(TrivyRunDto runDto) {
    var jobRunDto = jobRunRepository.get(runDto.getJobRunId());
    if (jobRunDto.podName() == null) {
      log.warn("trivy run {} has null podName. Can not store logs", runDto.getId());
      return;
    }
    log.info("Persisting missing logs for run: {}", runDto.getId());
    try (var logStream = k8sApi.streamPodLogs(jobRunDto.podName())) {
      logRepository.save(logStream, runDto.getId());
    } catch (IOException ioException) {
      log.error("Failed to persist run logs for run: {}", runDto.getId(), ioException);
    }
    runDto.setLogsStored(true);
    repository.save(runDto);
  }
}
