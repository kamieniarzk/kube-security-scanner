package com.kcs.k8s;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import io.kubernetes.client.PodLogs;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.util.Yaml;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.kcs.util.KubeApiUtils.*;
import static com.kcs.util.MiscUtils.*;

@Slf4j
@Component
public class KubernetesApiClientWrapper {
  private final CoreV1Api coreApi;
  private final BatchV1Api batchApi;
  private final PodLogs podLogs;

  public KubernetesApiClientWrapper(ApiClient apiClient) {
    this.coreApi = new CoreV1Api(apiClient);
    this.batchApi = new BatchV1Api(apiClient);
    this.podLogs = new PodLogs(apiClient);
  }

  public Optional<V1Pod> findPodByName(String name) {
    return getCurrentNamespacePods().getItems().stream()
        .filter(pod -> name.equals(pod.getMetadata().getName()))
        .findFirst();
  }

  public Optional<V1Job> findJobWithPrefix(String jobNamePrefix) {
    return performApiCall(() -> batchApi.listNamespacedJob(getCurrentNamespace(), null, null, null, null, null, null, null, null, null, null)).getItems().stream()
        .filter(job -> job.getMetadata().getName().startsWith(jobNamePrefix))
        .findFirst();
  }

  public V1Status deleteJob(V1Job v1Job) throws ApiException {
    return batchApi.deleteNamespacedJob(v1Job.getMetadata().getName(), v1Job.getMetadata().getNamespace(), null, null, null, null, null,
        null);
  }

  public List<V1Pod> getPodsWithPrefixSortedByCreation(String podNamePrefix) {
    var podList = getCurrentNamespacePods();
    return podList.getItems().stream()
        .filter(pod -> pod.getMetadata().getName().startsWith(podNamePrefix))
        .sorted(Comparator.comparing(pod -> ((V1Pod) pod).getMetadata().getCreationTimestamp()).reversed())
        .toList();
  }


  public V1Job createJobFromYamlUrl(String yamlUrl) {
    try {
      V1Job kubeBenchJob = (V1Job) Yaml.load(getFileFromUrl(yamlUrl));
      return performApiCall(() -> batchApi.createNamespacedJob(getCurrentNamespace(), kubeBenchJob, null, null, null, null));
    } catch (IOException ioException) {
      log.error("Could not obtain kube-bench job definition from GitHub");
      throw new RuntimeException();
    }
  }

  public InputStream getPodLogs(String name) {
    //    return performApiCall(() -> podLogs.streamNamespacedPodLog(getCurrentNamespace(), lastPodName, null));
    return performApiCall(() -> podLogs.streamNamespacedPodLog(getCurrentNamespace(), name, null));
  }

  @Nullable
  private V1PodList getCurrentNamespacePods() {
    return performApiCall(
        () -> coreApi.listNamespacedPod(getCurrentNamespace(), null, null, null, null, null, null, null, null, null, null));
  }
}
