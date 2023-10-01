package com.kcs.k8s;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import io.kubernetes.client.PodLogs;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.openapi.apis.RbacAuthorizationV1Api;
import io.kubernetes.client.openapi.models.V1ClusterRole;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1NetworkPolicy;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaim;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1RoleBinding;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceAccount;
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.util.Yaml;
import lombok.extern.slf4j.Slf4j;

import static com.kcs.util.KubeApiUtils.*;
import static com.kcs.util.MiscUtils.*;

@Slf4j
@Component
public class KubernetesApiClientWrapper {
  private final CoreV1Api coreApi;
  private final BatchV1Api batchApi;
  private final PodLogs podLogs;
  private final AppsV1Api appsApi;
  private final RbacAuthorizationV1Api rbacApi;
  private final NetworkingV1Api networkingApi;

  public KubernetesApiClientWrapper(ApiClient apiClient) {
    this.coreApi = new CoreV1Api(apiClient);
    this.batchApi = new BatchV1Api(apiClient);
    this.podLogs = new PodLogs(apiClient);
    this.appsApi = new AppsV1Api(apiClient);
    this.rbacApi = new RbacAuthorizationV1Api(apiClient);
    this.networkingApi = new NetworkingV1Api(apiClient);
  }

  public List<V1NetworkPolicy> getNetworkPoliciesByNamespace(String namespace) {
    return performApiCall(() -> networkingApi.listNamespacedNetworkPolicy(namespace, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1PersistentVolumeClaim> getPersistentVolumeClaimListByNamespace(String namespace) {
    return performApiCall(() -> coreApi.listNamespacedPersistentVolumeClaim(namespace, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1Job> getJobsByNamespace(String namespace) {
    return performApiCall(() -> batchApi.listNamespacedJob(namespace, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1ClusterRole> getClusterRoles() {
    return performApiCall(() -> rbacApi.listClusterRole(null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1RoleBinding> getRoleBindingsByNamespace(String namespace) {
    return performApiCall(() -> rbacApi.listNamespacedRoleBinding(namespace, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1ServiceAccount> getServiceAccountsByNamespace(String namespace) {
    return performApiCall(() -> coreApi.listNamespacedServiceAccount(namespace, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1Service> getServicesByNamespace(String namespace) {
    return performApiCall(() -> coreApi.listNamespacedService(namespace, null, null, null, null, null, null, null,null, null, null)).getItems();
  }

  public List<V1Deployment> getDeploymentsByNamespace(String namespace) {
    return performApiCall(() -> appsApi.listNamespacedDeployment(namespace, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1Pod> getPodsByNamespace(String namespace) {
    return getPods(namespace).getItems();
  }

  public Optional<V1Pod> findPod(String name, String namespace) {
    return getPods(namespace).getItems().stream()
        .filter(pod -> name.equals(pod.getMetadata().getName()))
        .findFirst();
  }

  public Optional<V1Deployment> findDeployment(String name, String namespace) {
    return performApiCall(() -> appsApi.listNamespacedDeployment(namespace, null, null, null, null, null, null, null, null, null, null)).getItems().stream()
        .filter(deployment -> name.equals(deployment.getMetadata().getName()))
        .findFirst();
  }

  public Optional<V1Job> findJob(String name, String namespace) {
    return performApiCall(() -> batchApi.listNamespacedJob(namespace, null, null, null, null, null, null, null, null, null, null)).getItems().stream()
        .filter(job -> job.getMetadata().getName().equals(name))
        .findFirst();
  }

  public Optional<V1Job> findJobWithPrefix(String jobNamePrefix) {
    return performApiCall(() -> batchApi.listNamespacedJob(getCurrentNamespace(), null, null, null, null, null, null, null, null, null, null)).getItems().stream()
        .filter(job -> job.getMetadata().getName().startsWith(jobNamePrefix))
        .findFirst();
  }

  public V1Status deleteJob(V1Job v1Job) throws ApiException {
    return batchApi.deleteNamespacedJob(v1Job.getMetadata().getName(), v1Job.getMetadata().getNamespace(), null, null, null, null, null,
        null);
  }

  public List<V1Pod> getPodsWithPrefixSortedByCreation(String podNamePrefix) {
    var podList = getCurrentNamespacePods();
    return podList.getItems().stream()
        .filter(pod -> pod.getMetadata().getName().startsWith(podNamePrefix))
        .sorted(Comparator.comparing(pod -> ((V1Pod) pod).getMetadata().getCreationTimestamp()).reversed())
        .toList();
  }


  public V1Job createJobFromYamlUrlWithModifiedCommand(String yamlUrl, String command) {
    try {
      V1Job kubeBenchJob = (V1Job) Yaml.load(getFileFromUrl(yamlUrl));
      kubeBenchJob.getSpec().getTemplate().getSpec().getContainers().get(0).setCommand(Arrays.stream(command.split(" ")).toList());
      return performApiCall(() -> batchApi.createNamespacedJob(getCurrentNamespace(), kubeBenchJob, null, null, null, null));
    } catch (IOException ioException) {
      log.error("Could not obtain kube-bench job definition from URL: {}", yamlUrl, ioException);
      throw new RuntimeException();
    }
  }

  public InputStream streamPodLogs(String name) {
    return performApiCall(() -> podLogs.streamNamespacedPodLog(getCurrentNamespace(), name, null));
  }

  public V1Job createJobWithContainerZeroArgs(String yamlUrl, List<String> args) {
    try {
      V1Job job = (V1Job) Yaml.load(getFileFromUrl(yamlUrl));
      job.getSpec().getTemplate().getSpec().getContainers().get(0).setArgs(args);
      return performApiCall(() -> batchApi.createNamespacedJob(getCurrentNamespace(), job, null, null, null, null));
    } catch (IOException ioException) {
      log.error("Could not obtain job definition from URL: {}", yamlUrl, ioException);
      throw new RuntimeException();
    }
  }

  @Nullable
  private V1PodList getPods(String namespace) {
    return performApiCall(
        () -> coreApi.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null, null));
  }

  @Nullable
  private V1PodList getCurrentNamespacePods() {
    return performApiCall(
        () -> coreApi.listNamespacedPod(getCurrentNamespace(), null, null, null, null, null, null, null, null, null, null));
  }
}
