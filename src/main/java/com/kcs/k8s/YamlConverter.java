package com.kcs.k8s;

import io.kubernetes.client.common.KubernetesObject;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Ingress;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1NetworkPolicy;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaim;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1RoleBinding;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceAccount;
import io.kubernetes.client.util.Yaml;

class YamlConverter {
  public static String convert(KubernetesObject object) {
    if (object instanceof V1Pod pod) return handle(pod);
    if (object instanceof V1Deployment deployment) return handle(deployment);
    if (object instanceof V1Service service) return handle(service);
    if (object instanceof V1Ingress ingress) return handle(ingress);
    if (object instanceof V1ServiceAccount serviceAccount) return handle(serviceAccount);
    if (object instanceof V1Job job) return handle(job);
    if (object instanceof V1NetworkPolicy networkPolicy) return handle(networkPolicy);
    if (object instanceof V1PersistentVolumeClaim pvc) return handle(pvc);
    if (object instanceof V1RoleBinding roleBinding) return handle(roleBinding);
    return Yaml.dump(object);
  }

  private static String handle(final V1RoleBinding object) {
    nullManagedFields(object);
    object.setApiVersion("v1");
    object.setKind("RoleBinding");
    return Yaml.dump(object);
  }

  private static String handle(final V1PersistentVolumeClaim object) {
    nullManagedFields(object);
    object.setApiVersion("v1");
    object.setKind("PersistentVolumeClaim");
    return Yaml.dump(object);
  }

  private static String handle(V1NetworkPolicy networkPolicy) {
    nullManagedFields(networkPolicy);
    networkPolicy.setApiVersion("v1");
    networkPolicy.setKind("NetworkPolicy");
    return Yaml.dump(networkPolicy);
  }

  private static String handle(final V1Job job) {
    nullManagedFields(job);
    job.setApiVersion("v1");
    job.setKind("Job");
    return Yaml.dump(job);
  }

  private static String handle(V1ServiceAccount serviceAccount) {
    nullManagedFields(serviceAccount);
    serviceAccount.setApiVersion("v1");
    serviceAccount.setKind("ServiceAccount");
    return Yaml.dump(serviceAccount);
  }

  private static String handle(V1Ingress ingress) {
    nullManagedFields(ingress);
    ingress.setApiVersion("v1");
    ingress.setKind("Ingress");
    ingress.setStatus(null);
    return Yaml.dump(ingress);
  }

  private static String handle(V1Service service) {
    nullManagedFields(service);
    service.setApiVersion("v1");
    service.setKind("Service");
    service.setStatus(null);
    return Yaml.dump(service);
  }

  private static String handle(V1Deployment deployment) {
    nullManagedFields(deployment);
    deployment.setApiVersion("v1");
    deployment.setKind("Deployment");
    deployment.setStatus(null);
    return Yaml.dump(deployment);
  }

  private static String handle(V1Pod pod) {
    nullManagedFields(pod);
    pod.setKind("Pod");
    pod.setApiVersion("v1");
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
