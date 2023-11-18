package com.kcs.score;

import com.kcs.k8s.KubernetesApiClientWrapper;
import com.kcs.k8s.YamlService;
import com.kcs.score.persistence.document.KubeScoreRepository;
import com.kcs.score.persistence.document.KubeScoreRunDto;
import com.kcs.util.ProcessRunner;
import io.kubernetes.client.common.KubernetesObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class OnHostBinaryKubeScoreRunner implements KubeScoreRunner {

  private static final String KUBE_SCORE_RUN_COMMAND_PATTERN = "kube-score score %s";
  private final KubernetesApiClientWrapper k8sApi;
  private final YamlService yamlService;
  private final KubeScoreRepository scoreRepository;

  @Transactional
  public String score(String namespace) {
    var savedYamlsLocation = yamlService.saveAsYamlInTempLocation(getObjectsList(namespace), namespace);
    return runKubeScoreBinary(KUBE_SCORE_RUN_COMMAND_PATTERN.formatted(savedYamlsLocation));
  }


  public List<KubeScoreRunDto> getByNamespace(String namespace) {
    return scoreRepository.getByNamespace(namespace);
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
    var pods = k8sApi.getPodsByNamespace(namespace);
    var deployments = k8sApi.getDeploymentsByNamespace(namespace);
    var services = k8sApi.getServicesByNamespace(namespace);
    var serviceAccounts = k8sApi.getServiceAccountsByNamespace(namespace);
    var jobs = k8sApi.getJobsByNamespace(namespace);
    var pvcs = k8sApi.getPersistentVolumeClaimListByNamespace(namespace);
    var roleBindings = k8sApi.getRoleBindingsByNamespace(namespace);
    var networkPolicies = k8sApi.getNetworkPoliciesByNamespace(namespace);
    objects.addAll(pods);
    objects.addAll(deployments);
    objects.addAll(services);
    objects.addAll(serviceAccounts);
    objects.addAll(jobs);
    objects.addAll(pvcs);
    objects.addAll(pods);
    objects.addAll(roleBindings);
    objects.addAll(networkPolicies);
    return objects;
  }
}
