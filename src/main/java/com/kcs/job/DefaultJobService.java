package com.kcs.job;

import com.kcs.k8s.KubernetesApiClientWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.kcs.util.KubeApiUtils.performApiCall;

@Slf4j
@Service
@RequiredArgsConstructor
class DefaultJobService implements JobService {

  private final JobRunRepository repository;
  private final KubernetesApiClientWrapper k8sApi;

  @Override
  public JobRunDto runJobFromUrlDefinitionWithModifiedCommand(String url, String podNamePrefix, String command) {
    deleteExistingJobIfNecessary(podNamePrefix);
    k8sApi.createJobFromYamlUrlWithModifiedCommand(url, command);
    return persistRunData(podNamePrefix);
  }

  @Override
  public JobRunDto runJobFromUrlDefinitionWithContainerArgs(String url, String namePrefix, String args) {
    deleteExistingJobIfNecessary(namePrefix);
    k8sApi.createJobWithContainerZeroArgs(url, Arrays.stream(args.split(" ")).toList());
    return persistRunData(namePrefix);
  }

  private JobRunDto persistRunData(String namePrefix) {
    log.info("Wait for job-related pod creation"); // TODO - use retryable instead of brute-force sleep
    sleep(3000);
    var jobPod = k8sApi.getPodsWithPrefixSortedByCreation(namePrefix).stream().findFirst();
    if (jobPod.isPresent()) {
      return repository.save(new JobRunCreate(jobPod.get().getMetadata().getName()));
    }
    log.warn("Failed to find pod related to {} job run run", namePrefix);
    return this.repository.save(new JobRunCreate(null));
  }

  private void deleteExistingJobIfNecessary(String namePrefix) {
    k8sApi.findJobWithPrefix(namePrefix)
        .ifPresent(v1Job -> performApiCall(() -> k8sApi.deleteJob(v1Job)));
  }

  void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException interruptedException) {
      log.warn("Interrupted thread sleep when waiting for job-related pod creation");
    }
  }
}
