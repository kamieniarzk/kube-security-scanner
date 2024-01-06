package com.kcs.bench;

import com.kcs.bench.persistence.KubeBenchRepository;
import com.kcs.bench.persistence.KubeBenchRunDto;
import com.kcs.job.JobRunRepository;
import com.kcs.k8s.KubernetesApiClientWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
class KubeBenchLogPersistence {

  private final KubeBenchRepository benchRepository;
  private final JobRunRepository jobRunRepository;
  private final KubernetesApiClientWrapper k8sApi;
  private final BenchLogRepository logRepository;

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
      logRepository.save(logStream, jobRunDto.podName());
    } catch (IOException ioException) {
      log.error("Failed to persist run logs for run: {}", benchRunDto.id(), ioException);
    }
    benchRepository.updateLogsStored(benchRunDto.id(), true);
  }
}
