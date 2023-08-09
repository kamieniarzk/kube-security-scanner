package com.kcs.k8s;

import org.springframework.stereotype.Service;

import com.kcs.NoDataFoundException;

import io.kubernetes.client.common.KubernetesObject;
import io.kubernetes.client.util.Yaml;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class JavaClientYamlService implements YamlService {

  private final KubernetesApiClientWrapper k8sApi;

  @Override
  public String getAsYaml(String name, String namespace, K8sResourceType resourceType) {
    return switch (resourceType) {
      case POD -> getPodAsYaml(name, namespace);
      case DEPLOYMENT -> getDeploymentAsYaml(name, namespace);
      default -> throw new UnsupportedOperationException();
    };
  }

  private String getDeploymentAsYaml(String name, String namespace) {
    var deployment = k8sApi.findDeployment(name, namespace);
    if (deployment.isPresent()) {
      var deploymentObject = deployment.get();
      nullManagedFields(deploymentObject);
      deploymentObject.setApiVersion("V1");
      deploymentObject.setKind("Deployment");
      deploymentObject.setStatus(null);
      return Yaml.dump(deploymentObject);
    }
    throw new NoDataFoundException();
  }

  private String getPodAsYaml(String name, String namespace) {
    var pod = k8sApi.findPod(name, namespace);
    if (pod.isPresent()) {
      var podObject = pod.get();
      nullManagedFields(pod.get());
      podObject.setKind("Pod");
      podObject.setApiVersion("V1");
      podObject.setStatus(null);
      return Yaml.dump(podObject);
    }
    throw new NoDataFoundException();
  }

  private static void nullManagedFields(KubernetesObject object) {
    if (object.getMetadata() != null) {
      object.getMetadata().setManagedFields(null);
      object.getMetadata().setUid(null);
      object.getMetadata().setAnnotations(null);
    }
  }
}
