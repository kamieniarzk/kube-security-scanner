package com.kcs.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kcs.bench.KubeBenchService;
import com.kcs.k8s.YamlService;
import com.kcs.bench.persistence.log.LogService;
import com.kcs.score.KubeScoreService;
import com.kcs.bench.persistence.dto.KubeBenchRepository;
import com.kcs.bench.persistence.dto.KubeBenchRun;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Yaml;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/scans")
public class ScanController {
  private final CoreV1Api coreApi;
  private final KubeBenchService kubeBenchService;
  private final KubeBenchRepository kubeBenchRepository;
  private final LogService logService;
  private final YamlService yamlService;
  private final KubeScoreService kubeScoreService;

  public ScanController(ApiClient apiClient, KubeBenchService kubeBenchService,
      KubeBenchRepository kubeBenchRepository, LogService logService,
      YamlService yamlService, KubeScoreService kubeScoreService) {
    this.coreApi = new CoreV1Api(apiClient);
    this.kubeBenchService = kubeBenchService;
    this.kubeBenchRepository = kubeBenchRepository;
    this.logService = logService;
    this.yamlService = yamlService;
    this.kubeScoreService = kubeScoreService;
  }

  @PostMapping("/kube-score/{namespace}")
  String score(@PathVariable String namespace) throws IOException, InterruptedException {
//    return ProcessRunner.run("ls -ltr").stdIn();
    return kubeScoreService.score(namespace);
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
  public String runKubeBench() {
    return this.kubeBenchService.run();
  }

  @GetMapping("lastLogs")
  String lastRunLogs() throws IOException {
    return new String(kubeBenchService.getPreviousRunLogs().readAllBytes(), StandardCharsets.UTF_8);
  }
  @GetMapping
  public List<KubeBenchRun> getAllScans() {
    return kubeBenchRepository.getAll();
  }

  @GetMapping("/{scanId}")
  public KubeBenchRun getById(@PathVariable String scanId) {
    return kubeBenchRepository.get(scanId);
  }

  @GetMapping("watch-test")
  public void watchTest() {
    logService.persistRunLogsForRunsWithoutStoredLogs();
  }
}
