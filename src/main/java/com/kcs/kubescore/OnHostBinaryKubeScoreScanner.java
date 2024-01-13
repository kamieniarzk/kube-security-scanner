package com.kcs.kubescore;

import com.kcs.apiserver.ApiServerClient;
import com.kcs.util.ProcessRunner;
import io.kubernetes.client.common.KubernetesObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class OnHostBinaryKubeScoreScanner implements KubeScoreScanner {

  private static final String KUBE_SCORE_RUN_COMMAND_PATTERN = "kube-score score %s -o json %s";
  private final ApiServerClient k8sApi;
  private final YamlStorage yamlStorage;

  @Override
  public String score(String namespace, String additionalFlags) {
    var savedYamlsLocation = yamlStorage.saveAsYamlInTempLocation(getObjectsList(namespace), namespace);
    return runKubeScoreBinary(KUBE_SCORE_RUN_COMMAND_PATTERN.formatted(savedYamlsLocation, additionalFlags == null ? "" : additionalFlags));
  }

  @Override
  public String scoreAllNamespaces(String additionalFlags) {
    var allNamespaces = k8sApi.getAllClusterNamespaces().stream()
        .map(namespace -> namespace.getMetadata().getName())
        .toList();

    var allResources = allNamespaces.stream()
        .map(this::getObjectsList)
        .flatMap(Collection::stream)
        .toList();

    var savedYamlsLocation = yamlStorage.saveAsYamlInTempLocation(allResources);
    return runKubeScoreBinary(KUBE_SCORE_RUN_COMMAND_PATTERN.formatted(savedYamlsLocation, additionalFlags == null ? "" : additionalFlags));
  }

  private static String runKubeScoreBinary(final String command) {
    try {
      var runResult = ProcessRunner.run(command);
      if (runResult.stdErr() != null && !runResult.stdErr().isBlank()) {
        throw new RuntimeException();
      }
      return runResult.stdIn();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private List<KubernetesObject> getObjectsList(final String namespace) {
    var objects = new ArrayList<KubernetesObject>();
    objects.addAll(k8sApi.getPodsByNamespace(namespace));
    objects.addAll(k8sApi.getDeploymentsByNamespace(namespace));
    objects.addAll(k8sApi.getReplicaSetsByNamespace(namespace));
    objects.addAll(k8sApi.getReplicationControllersByNamespace(namespace));
    objects.addAll(k8sApi.getStatefulSetsByNamespace(namespace));
    objects.addAll(k8sApi.getDaemonSetsByNamespace(namespace));
    objects.addAll(k8sApi.getCronJobsByNamespace(namespace));
    objects.addAll(k8sApi.getServicesByNamespace(namespace));
    objects.addAll(k8sApi.getServiceAccountsByNamespace(namespace));
    objects.addAll(k8sApi.getJobsByNamespace(namespace));
    objects.addAll(k8sApi.getConfigMapsByNamespace(namespace));
    objects.addAll(k8sApi.getRolesByNamespace(namespace));
    objects.addAll(k8sApi.getIngressesByNamespace(namespace));
    objects.addAll(k8sApi.getResourceQuotasByNamespace(namespace));
    objects.addAll(k8sApi.getLimitRangesByNamespace(namespace));
    objects.addAll(k8sApi.getPersistentVolumeClaimListByNamespace(namespace));
    objects.addAll(k8sApi.getPodsByNamespace(namespace));
    objects.addAll(k8sApi.getRoleBindingsByNamespace(namespace));
    objects.addAll(k8sApi.getNetworkPoliciesByNamespace(namespace));
    return objects;
  }
}
