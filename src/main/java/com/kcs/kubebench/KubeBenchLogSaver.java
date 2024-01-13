package com.kcs.kubebench;

import com.kcs.apiserver.ApiServerClient;
import com.kcs.kubebench.persistence.KubeBenchRepository;
import com.kcs.kubebench.persistence.KubeBenchScanDto;
import com.kcs.job.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class KubeBenchLogSaver {

  private final KubeBenchRepository benchRepository;
  private final JobRepository jobRepository;
  private final ApiServerClient k8sApi;
  private final BenchLogRepository logRepository;

  public void persistLogs() {
    benchRepository.getAllWithoutStoredLogs()
        .forEach(this::persistRunLogs);
  }

  private void persistRunLogs(KubeBenchScanDto benchRunDto) { // TODO (optional) - extract the pull based log persistence abstraction for job-based runs (this and trivy)
    var jobRunDto = jobRepository.get(benchRunDto.jobRunId());
    if (jobRunDto.podName() == null) {
      log.warn("kube-bench run {} has null podName. Can not store logs", benchRunDto.id());
      return;
    }
    log.info("Persisting missing logs for run: {}", benchRunDto.id());
    try (var logStream = k8sApi.streamPodLogs(jobRunDto.podName())) {
      logRepository.save(logStream, benchRunDto.id());
    } catch (Exception any) {
      log.error("Failed to persist run logs for run: {}", benchRunDto.id(), any);
      return;
    }
    benchRepository.updateLogsStored(benchRunDto.id(), true);
  }
}
