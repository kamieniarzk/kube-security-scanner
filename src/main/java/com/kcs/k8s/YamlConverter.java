package com.kcs.k8s;

import com.kcs.score.KubeScoreService;

import io.kubernetes.client.common.KubernetesObject;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Ingress;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceAccount;
import io.kubernetes.client.util.Yaml;

public class YamlConverter {
  public static String convert(KubernetesObject object) {
//    if (object instanceof V1Pod pod) return handle(pod);
//    if (object instanceof V1Deployment deployment) return handle(deployment);
//    if (object instanceof V1Service service) return handle(service);
//    if (object instanceof V1Ingress ingress) return handle(ingress);
//    if (object instanceof V1ServiceAccount serviceAccount) return handle(serviceAccount);
    return Yaml.dump(object);
  }

  private static String handle(V1ServiceAccount serviceAccount) {
    nullManagedFields(serviceAccount);
    serviceAccount.setApiVersion("V1");
    serviceAccount.setKind("ServiceAccount");
    return Yaml.dump(serviceAccount);
  }

  private static String handle(V1Ingress ingress) {
    nullManagedFields(ingress);
    ingress.setApiVersion("V1");
    ingress.setKind("Ingress");
    ingress.setStatus(null);
    return Yaml.dump(ingress);
  }

  private static String handle(V1Service service) {
    nullManagedFields(service);
    service.setApiVersion("V1");
    service.setKind("Service");
    service.setStatus(null);
    return Yaml.dump(service);
  }

  private static String handle(V1Deployment deployment) {
    nullManagedFields(deployment);
    deployment.setApiVersion("V1");
    deployment.setKind("Deployment");
    deployment.setStatus(null);
    return Yaml.dump(deployment);
  }

  private static String handle(V1Pod pod) {
    nullManagedFields(pod);
    pod.setKind("Pod");
    pod.setApiVersion("V1");
    pod.setStatus(null);
    return Yaml.dump(pod);
  }

  private static void nullManagedFields(KubernetesObject object) {
    if (object.getMetadata() != null) {
      object.getMetadata().setManagedFields(null);
      object.getMetadata().setUid(null);
      object.getMetadata().setAnnotations(null);
    }
  }
}
