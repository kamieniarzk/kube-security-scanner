package com.kcs.job;

import com.kcs.apiserver.ApiServerClient;
import com.kcs.context.ContextHolder;
import com.kcs.util.MiscUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
class DefaultJobService implements JobService {

  private final JobRepository repository;
  private final ApiServerClient k8sApi;
  private final ContextHolder contextHolder;

  @Override
  public JobDto runJobFromUrlDefinitionWithModifiedCommand(String url, String podNamePrefix, String command) {
    deleteExistingJobIfNecessary(podNamePrefix);
    k8sApi.createJobFromYamlUrlWithModifiedCommand(url, command);
    return persistRunData(podNamePrefix);
  }

  @Override
  public JobDto runJobFromUrlDefinitionWithContainerArgs(String url, String namePrefix, String args) {
    deleteExistingJobIfNecessary(namePrefix);
    k8sApi.createJobWithContainerZeroArgs(url, Arrays.stream(args.split(" ")).toList());
    return persistRunData(namePrefix);
  }

  @Override
  public JobDto runJobFromUrlDefinitionWithContextServiceAccount(String yaml, String podNamePrefix) {
    deleteExistingJobIfNecessary(podNamePrefix);
    try {
      k8sApi.createJobFromYamlWithServiceAccount(yaml, MiscUtils.constructServiceAccountName(contextHolder.getHelmReleaseName()));
    } catch (IOException ioException) {
      throw new RuntimeException(ioException);
    }
    return persistRunData(podNamePrefix);
  }

  private JobDto persistRunData(String namePrefix) {
    log.info("Wait for job-related pod creation"); // TODO - use retryable instead of brute-force sleep
    sleep(3000);
    var jobPod = k8sApi.getPodsWithPrefixSortedByCreation(namePrefix).stream().findFirst();
    if (jobPod.isPresent()) {
      return repository.save(new JobCreate(jobPod.get().getMetadata().getName()));
    }
    log.warn("Failed to find pod related to {} job run run", namePrefix);
    return this.repository.save(new JobCreate(null));
  }

  private void deleteExistingJobIfNecessary(String namePrefix) {
    k8sApi.findJobWithPrefix(namePrefix)
        .ifPresent(v1Job -> {
          log.info("Deleting job {} and waiting 3 seconds", v1Job.getMetadata().getName());
          k8sApi.deleteJob(v1Job);
          sleep(3000);
        });
  }

  void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException interruptedException) {
      log.warn("Interrupted thread sleep when waiting for job-related pod creation");
    }
  }
}
