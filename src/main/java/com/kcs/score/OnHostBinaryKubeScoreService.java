package com.kcs.score;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.kcs.k8s.KubernetesApiClientWrapper;
import com.kcs.k8s.YamlService;
import com.kcs.util.ProcessRunner;

import io.kubernetes.client.common.KubernetesObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class OnHostBinaryKubeScoreService implements KubeScoreService {

  private final KubernetesApiClientWrapper k8sApi;
  private final YamlService yamlService;

  @Override
  public String score(String namespace) {
    List<KubernetesObject> objects = new ArrayList<>();
    var pods = k8sApi.getPodsByNamespace(namespace);
    var deployments = k8sApi.getDeploymentsByNamespace(namespace);
    var services = k8sApi.getServicesByNamespace(namespace);
    var serviceAccounts = k8sApi.getServiceAccountsByNamespace(namespace);
    var jobs = k8sApi.getJobsByNamespace(namespace);
    var roleBindings = k8sApi.getRoleBindingsByNamespace(namespace);
    objects.addAll(pods);
    objects.addAll(deployments);
    objects.addAll(services);
    objects.addAll(serviceAccounts);
    objects.addAll(jobs);
    objects.addAll(roleBindings);
    var savedYamlsLocation = yamlService.saveAsYamlInTempLocation(objects, namespace);

    var args = String.join(" ", savedYamlsLocation);

    try {
      var runResult = ProcessRunner.run("kube-score score " + args);
      if (runResult.exitCode() != 0) {
        throw new RuntimeException();
      }
      return runResult.stdIn();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
