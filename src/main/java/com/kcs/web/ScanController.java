package com.kcs.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;
import com.kcs.bench.KubeBenchService;
import com.kcs.k8s.K8sResourceType;
import com.kcs.k8s.YamlService;
import com.kcs.log.LogService;
import com.kcs.shared.ScanRepository;
import com.kcs.shared.ScanRun;
import com.kcs.util.MiscUtils;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.util.Watch;
import io.kubernetes.client.util.Watch.Response;
import io.kubernetes.client.util.Yaml;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/scans")
public class ScanController {
  private final CoreV1Api coreApi;
  private final KubeBenchService kubeBenchService;
  private final ScanRepository scanRepository;
  private final LogService logService;
  private final YamlService yamlService;

  public ScanController(ApiClient apiClient, KubeBenchService kubeBenchService, ScanRepository scanRepository, LogService logService, YamlService yamlService) {
    this.coreApi = new CoreV1Api(apiClient);
    this.kubeBenchService = kubeBenchService;
    this.scanRepository = scanRepository;
    this.logService = logService;
    this.yamlService = yamlService;
  }

  @GetMapping("/yaml-test/{namespace}/{name}")
  String getResourceAsYaml(@PathVariable String namespace, @PathVariable String name, @RequestParam K8sResourceType resourceType) {
    return yamlService.getAsYaml(name, namespace, resourceType);
  }

  @GetMapping("/test")
  public String getNodes() {
    try {
      return coreApi.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null).getItems().stream()
          .map(Yaml::dump).findFirst().orElse("");
    } catch (ApiException apiException) {
      log.warn("ApiException caught - response code: {}, response body: {}", apiException.getCode(), apiException.getResponseBody());
      return "";
    }
  }

  @PostMapping
  public String runKubeBench() throws IOException {
    return this.kubeBenchService.run();
  }

  @GetMapping("lastLogs")
  String lastRunLogs() throws IOException {
    return new String(kubeBenchService.getPreviousRunLogs().readAllBytes(), StandardCharsets.UTF_8);
  }
  @GetMapping
  public List<ScanRun> getAllScans() {
    return scanRepository.getAll();
  }

  @GetMapping("/{scanId}")
  public ScanRun getById(@PathVariable String scanId) {
    return scanRepository.get(scanId);
  }

  @GetMapping("watch-test")
  public void watchTest() {
    logService.persistRunLogsForRunsWithoutStoredLogs();
  }
}
