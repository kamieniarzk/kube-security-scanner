package com.kcs.trivy;

import com.kcs.job.JobRepository;
import com.kcs.apiserver.ApiServerClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
class TrivyLogService {

  private final TrivyScanRepository repository;
  private final JobRepository jobRepository;
  private final ApiServerClient k8sApi;
  private final TrivyLogRepository logRepository;

  public void persistLogs() {
    repository.findWhereLogsStoredNullOrFalse()
        .forEach(this::persistRunLogs);
  }

  private void persistRunLogs(TrivyScanDto runDto) {
    var jobRunDto = jobRepository.get(runDto.getJobRunId());
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
