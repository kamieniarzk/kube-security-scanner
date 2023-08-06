package com.kcs.bench;

import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.kcs.shared.ScanRepository;
import com.kcs.shared.ScanRun;
import com.kcs.shared.ScanType;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.util.Yaml;
import lombok.extern.slf4j.Slf4j;

import static com.kcs.util.KubeApiUtils.*;
import static com.kcs.util.MiscUtils.*;

@Slf4j
@Service
public class DefaultKubeBenchService implements KubeBenchService {

  private final CoreV1Api coreApi;
  private final BatchV1Api batchApi;
  private final ScanRepository scanRepository;

  public DefaultKubeBenchService(ApiClient apiClient, ScanRepository scanRepository) {
    coreApi = new CoreV1Api(apiClient);
    batchApi = new BatchV1Api(apiClient);
    this.scanRepository = scanRepository;
  }

  @Override
  public String run() {
    deleteExistingJobIfNecessary();

    try {
      V1Job kubeBenchJob = (V1Job) Yaml.load(getFileFromUrl("https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml"));
      var createdJob = performApiCall(() -> batchApi.createNamespacedJob(getCurrentNamespace(), kubeBenchJob, null, null, null, null));
    } catch (IOException ioException) {
      log.error("Could not obtain kube-bench job definition from GitHub");
      throw new RuntimeException();
    }
    var podList = performApiCall(() -> coreApi.listNamespacedPod("kube-config-scanner", null, null, null, null, null, null, null, null, null, null));
    var kubeBenchPod = podList.getItems().stream().filter(pod -> pod.getMetadata().getName().startsWith("kube-bench")).sorted(
            Comparator.comparing(pod -> pod.getMetadata().getCreationTimestamp()))
        .findFirst().get();


    return this.scanRepository.save(new ScanRun(ScanType.KUBE_BENCH, kubeBenchPod.getMetadata().getName()));
  }

  @Override
  public String getPreviousRunLogs() {
    return null;
  }

  private void deleteExistingJobIfNecessary() {
    existingKubeBenchJob()
        .ifPresent(v1Job -> performApiCall(
            () -> batchApi.deleteNamespacedJob(v1Job.getMetadata().getName(), v1Job.getMetadata().getNamespace(), null, null, null, null, null,
                null)));
  }

  private Optional<V1Job> existingKubeBenchJob() {
    return performApiCall(() -> batchApi.listNamespacedJob(getCurrentNamespace(), null, null, null, null, null, null, null, null, null, null)).getItems().stream()
        .filter(job -> job.getMetadata().getName().startsWith("kube-bench"))
        .findFirst();
  }
}
