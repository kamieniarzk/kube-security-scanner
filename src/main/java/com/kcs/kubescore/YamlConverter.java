package com.kcs.kubescore;

import io.kubernetes.client.common.KubernetesObject;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Yaml;

class YamlConverter {
  public static String convert(KubernetesObject object) {
    return switch (object) {
      case V1Pod pod -> handle(pod);
      case V1Deployment deployment -> handle(deployment);
      case V1Service service -> handle(service);
      case V1Ingress ingress -> handle(ingress);
      case V1ServiceAccount serviceAccount -> handle(serviceAccount);
      case V1Job job -> handle(job);
      case V1NetworkPolicy networkPolicy -> handle(networkPolicy);
      case V1PersistentVolumeClaim pvc -> handle(pvc);
      case V1RoleBinding roleBinding -> handle(roleBinding);
      case V1ReplicaSet replicaSet -> handle(replicaSet);
      case V1ReplicationController replicationController -> handle(replicationController);
      case V1StatefulSet statefulSet -> handle(statefulSet);
      case V1DaemonSet daemonSet -> handle(daemonSet);
      case V1CronJob cronJob -> handle(cronJob);
      case V1ConfigMap configMap -> handle(configMap);
      case V1Role role -> handle(role);
      case V1ResourceQuota resourceQuota -> handle(resourceQuota);
      case V1LimitRange limitRange -> handle(limitRange);
      default -> Yaml.dump(object);
    };
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

  private static String handle(V1ReplicaSet replicaSet) {
    nullManagedFields(replicaSet);
    replicaSet.setKind("ReplicaSet");
    replicaSet.setApiVersion("v1");
    replicaSet.setStatus(null);
    return Yaml.dump(replicaSet);
  }

  private static String handle(V1ReplicationController replicationController) {
    nullManagedFields(replicationController);
    replicationController.setKind("ReplicationController");
    replicationController.setApiVersion("v1");
    replicationController.setStatus(null);
    return Yaml.dump(replicationController);
  }

  private static String handle(V1StatefulSet statefulSet) {
    nullManagedFields(statefulSet);
    statefulSet.setKind("StatefulSet");
    statefulSet.setApiVersion("v1");
    statefulSet.setStatus(null);
    return Yaml.dump(statefulSet);
  }

  private static String handle(V1DaemonSet daemonSet) {
    nullManagedFields(daemonSet);
    daemonSet.setKind("DaemonSet");
    daemonSet.setApiVersion("v1");
    daemonSet.setStatus(null);
    return Yaml.dump(daemonSet);
  }

  private static String handle(V1CronJob cronJob) {
    nullManagedFields(cronJob);
    cronJob.setKind("CronJob");
    cronJob.setApiVersion("v1");
    cronJob.setStatus(null);
    return Yaml.dump(cronJob);
  }

  private static String handle(V1ConfigMap configMap) {
    nullManagedFields(configMap);
    configMap.setKind("ConfigMap");
    configMap.setApiVersion("v1");
    return Yaml.dump(configMap);
  }

  private static String handle(V1Role role) {
    nullManagedFields(role);
    role.setKind("Role");
    role.setApiVersion("v1");
    return Yaml.dump(role);
  }

  private static String handle(V1ResourceQuota resourceQuota) {
    nullManagedFields(resourceQuota);
    resourceQuota.setKind("ResourceQuota");
    resourceQuota.setApiVersion("v1");
    resourceQuota.setStatus(null);
    return Yaml.dump(resourceQuota);
  }

  private static String handle(V1LimitRange limitRange) {
    nullManagedFields(limitRange);
    limitRange.setKind("DaemonSet");
    limitRange.setApiVersion("v1");
    return Yaml.dump(limitRange);
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
      object.getMetadata().setCreationTimestamp(null);
    }
  }
}
