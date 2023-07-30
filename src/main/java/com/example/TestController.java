package com.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.lang.Nullable;
import org.springframework.util.function.ThrowingSupplier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Yaml;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TestController {
  private final CoreV1Api coreApi;
  private final BatchV1Api batchApi;

  public TestController(ApiClient apiClient) {
    coreApi = new CoreV1Api(apiClient);
    batchApi = new BatchV1Api(apiClient);
  }

  @GetMapping("/test")
  public List<String> getNodes() {
    try {
      return coreApi.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null).getItems().stream()
          .map(V1Pod::getSpec).filter(Objects::nonNull).map(V1PodSpec::getNodeName).toList();
    } catch (ApiException apiException) {
      log.warn("ApiException caught - response code: {}, response body: {}", apiException.getCode(), apiException.getResponseBody());
      return Collections.emptyList();
    }
  }

  @PostMapping
  public V1Pod runKubeBench() throws IOException {
    deleteExistingJobIfNecessary();

    V1Job kubeBenchJob = (V1Job) Yaml.load(getFileFromUrl("https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml"));
    var createdJob = performApiCall(() -> batchApi.createNamespacedJob(getCurrentNamespace(), kubeBenchJob, null, null, null, null));
//    api.createNamespacedJob()
    log.info("i co");
    var podList = performApiCall(() -> coreApi.listNamespacedPod("kube-config-scanner", null, null, null, null, null, null, null, null, null, null));
    return podList.getItems().stream().filter(pod -> pod.getMetadata().getName().startsWith("kube-bench")).sorted(Comparator.comparing(pod -> pod.getMetadata().getCreationTimestamp()))
        .findFirst().get();


//    var job = new V1JobBuilder().
  }

  private void deleteExistingJobIfNecessary() {
    existingKubeBenchJob()
    .ifPresent(v1Job -> performApiCall(
        () -> batchApi.deleteNamespacedJob(v1Job.getMetadata().getName(), v1Job.getMetadata().getNamespace(), null, null, null, null, null,
            null)));
  }

  Optional<V1Job> existingKubeBenchJob() {
    return performApiCall(() -> batchApi.listNamespacedJob(getCurrentNamespace(), null, null, null, null, null, null, null, null, null, null)).getItems().stream()
        .filter(job -> job.getMetadata().getName().startsWith("kube-bench"))
        .findFirst();
  }

//  @GetMapping
//  Object get(){
//
//  }

  File getFileFromUrl(String stringUrl) throws IOException {
    URL url = new URL(stringUrl);

    File tempFile = File.createTempFile("prefix", "suffix");
    tempFile.deleteOnExit();

    try (InputStream in = url.openStream()) {
      Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    return tempFile;
  }

  String getCurrentNamespace() {
    try {
      return readFileToString("/var/run/secrets/kubernetes.io/serviceaccount/namespace");
    } catch (IOException exception) {
      log.debug("Failed to read current namespace, assuming default");
      return "kube-config-scanner";
    }
  }

  public static String readFileToString(String filePath) throws IOException {
    return new String(Files.readAllBytes(Paths.get(filePath)));
  }

  @Nullable
  <T> T performApiCall(ThrowingSupplier<T> apiCall) {
    try {
      return apiCall.get();
    } catch (Exception exception) {
      if (exception instanceof ApiException apiException) {
        log.warn("ApiException caught, status: {}, body: {}", apiException.getCode(), apiException.getResponseBody());
      }
      throw new RuntimeException(exception);
    }
  }
}
