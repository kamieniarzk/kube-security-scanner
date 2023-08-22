package com.kcs.score;

import com.kcs.k8s.KubernetesApiClientWrapper;
import com.kcs.k8s.YamlService;
import com.kcs.score.persistence.document.KubeScoreRepository;
import com.kcs.score.persistence.document.KubeScoreRun;
import com.kcs.score.persistence.document.KubeScoreRunCreate;
import com.kcs.shared.LogRepository;
import com.kcs.util.ProcessRunner;
import io.kubernetes.client.common.KubernetesObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
class OnHostBinaryKubeScoreService implements KubeScoreService {

  private final KubernetesApiClientWrapper k8sApi;
  private final YamlService yamlService;
  private final KubeScoreRepository scoreRepository;
  private final LogRepository logRepository;
  private final String scoreDirectory;

  OnHostBinaryKubeScoreService(KubernetesApiClientWrapper k8sApi, YamlService yamlService, KubeScoreRepository scoreRepository,
                               LogRepository logRepository,
                               @Value("${filesystem.score-location:/tmp/kube-config-scanner/score}") String scoreDirectory) {
    this.k8sApi = k8sApi;
    this.yamlService = yamlService;
    this.scoreRepository = scoreRepository;
    this.logRepository = logRepository;
    this.scoreDirectory = scoreDirectory;
  }


  @Override
  @Transactional
  public String score(String namespace) {
    var savedYamlsLocation = yamlService.saveAsYamlInTempLocation(getObjectsList(namespace), namespace);
    var args = String.join(" ", savedYamlsLocation);
    var score = runKubeScoreBinary(args);
    var scoreId = scoreRepository.save(new KubeScoreRunCreate(namespace));
    logRepository.save(score, scoreDirectory, scoreId);
    return scoreId;
  }

  @Override
  public List<KubeScoreRun> getByNamespace(String namespace) {
    return scoreRepository.getByNamespace(namespace);
  }

  private static String runKubeScoreBinary(final String args) {
    try {
      var runResult = ProcessRunner.run("kube-score score " + args);
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
