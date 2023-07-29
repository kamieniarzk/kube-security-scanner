package com.example;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.util.Config;

@RestController
public class TestController {
  private final ApiClient client;
  private final CoreV1Api coreV1Api;

  public TestController() throws IOException {
    client = Config.defaultClient();
    coreV1Api = new CoreV1Api(client);
  }

  @GetMapping("/test")
  public List<String> getNodes() throws ApiException {
    return coreV1Api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null).getItems().stream()
        .map(V1Pod::getSpec).filter(Objects::nonNull).map(V1PodSpec::getNodeName).toList();
  }
}
