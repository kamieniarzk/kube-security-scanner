package com.kcs.apiserver;

import io.kubernetes.client.PodLogs;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.*;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ModelMapper;
import io.kubernetes.client.util.Yaml;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.kcs.apiserver.KubeApiUtils.apiCall;
import static com.kcs.util.MiscUtils.getCurrentNamespace;
import static com.kcs.util.MiscUtils.getFileFromUrl;

@Slf4j
@Component
public class ApiServerClient {
  private final CoreV1Api coreApi;
  private final BatchV1Api batchApi;
  private final PodLogs podLogs;
  private final AppsV1Api appsApi;
  private final RbacAuthorizationV1Api rbacApi;
  private final NetworkingV1Api networkingApi;

  public ApiServerClient(ApiClient apiClient) {
    ModelMapper.addModelMap("batch", "v1", "Job", "jobs", V1Job.class, V1JobList.class);
    this.coreApi = new CoreV1Api(apiClient);
    this.batchApi = new BatchV1Api(apiClient);
    this.podLogs = new PodLogs(apiClient);
    this.appsApi = new AppsV1Api(apiClient);
    this.rbacApi = new RbacAuthorizationV1Api(apiClient);
    this.networkingApi = new NetworkingV1Api(apiClient);
  }

  public List<V1NetworkPolicy> getNetworkPoliciesByNamespace(String namespace) {
    return apiCall(() -> networkingApi.listNamespacedNetworkPolicy(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1PersistentVolumeClaim> getPersistentVolumeClaimListByNamespace(String namespace) {
    return apiCall(() -> coreApi.listNamespacedPersistentVolumeClaim(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1Job> getJobsByNamespace(String namespace) {
    return apiCall(() -> batchApi.listNamespacedJob(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1ClusterRole> getClusterRoles() {
    return apiCall(() -> rbacApi.listClusterRole(null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1RoleBinding> getRoleBindingsByNamespace(String namespace) {
    return apiCall(() -> rbacApi.listNamespacedRoleBinding(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1ServiceAccount> getServiceAccountsByNamespace(String namespace) {
    return apiCall(() -> coreApi.listNamespacedServiceAccount(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1Service> getServicesByNamespace(String namespace) {
    return apiCall(() -> coreApi.listNamespacedService(namespace, null, null, null, null, null, null, null,null, null, null, null)).getItems();
  }

  public List<V1Deployment> getDeploymentsByNamespace(String namespace) {
    return apiCall(() -> appsApi.listNamespacedDeployment(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1Pod> getPodsByNamespace(String namespace) {
    return getPods(namespace).getItems();
  }

  public List<V1ReplicaSet> getReplicaSetsByNamespace(String namespace) {
    return apiCall(() -> appsApi.listNamespacedReplicaSet(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1ReplicationController> getReplicationControllersByNamespace(String namespace) {
    return apiCall(() -> coreApi.listNamespacedReplicationController(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1StatefulSet> getStatefulSetsByNamespace(String namespace) {
    return apiCall(() -> appsApi.listNamespacedStatefulSet(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1DaemonSet> getDaemonSetsByNamespace(String namespace) {
    return apiCall(() -> appsApi.listNamespacedDaemonSet(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1CronJob> getCronJobsByNamespace(String namespace) {
    return apiCall(() -> batchApi.listNamespacedCronJob(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1ConfigMap> getConfigMapsByNamespace(String namespace) {
    return apiCall(() -> coreApi.listNamespacedConfigMap(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1Role> getRolesByNamespace(String namespace) {
    return apiCall(() -> rbacApi.listNamespacedRole(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1Ingress> getIngressesByNamespace(String namespace) {
    return apiCall(() -> networkingApi.listNamespacedIngress(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1ResourceQuota> getResourceQuotasByNamespace(String namespace) {
    return apiCall(() -> coreApi.listNamespacedResourceQuota(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1LimitRange> getLimitRangesByNamespace(String namespace) {
    return apiCall(() -> coreApi.listNamespacedLimitRange(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public List<V1Namespace> getAllClusterNamespaces() {
    return apiCall(() -> coreApi.listNamespace(null, null, null, null, null, null, null, null, null, null, null)).getItems();
  }

  public Optional<V1Pod> findPod(String name, String namespace) {
    return getPods(namespace).getItems().stream()
        .filter(pod -> name.equals(pod.getMetadata().getName()))
        .findFirst();
  }

  public Optional<V1Deployment> findDeployment(String name, String namespace) {
    return apiCall(() -> appsApi.listNamespacedDeployment(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems().stream()
        .filter(deployment -> name.equals(deployment.getMetadata().getName()))
        .findFirst();
  }

  public Optional<V1Job> findJob(String name, String namespace) {
    return apiCall(() -> batchApi.listNamespacedJob(namespace, null, null, null, null, null, null, null, null, null, null, null)).getItems().stream()
        .filter(job -> job.getMetadata().getName().equals(name))
        .findFirst();
  }

  public Optional<V1Job> findJobWithPrefix(String jobNamePrefix) {
    return apiCall(() -> batchApi.listNamespacedJob(getCurrentNamespace(), null, null, null, null, null, null, null, null, null, null, null)).getItems().stream()
        .filter(job -> job.getMetadata().getName().startsWith(jobNamePrefix))
        .findFirst();
  }

  public Optional<V1Node> getAnyNode() {
    return apiCall(() -> coreApi.listNode(null, null, null, null, null, null, null, null, null, null, null)).getItems().stream()
        .findAny();
  }

  public V1Status deleteJob(V1Job v1Job) {
    return apiCall(() -> batchApi.deleteNamespacedJob(v1Job.getMetadata().getName(), v1Job.getMetadata().getNamespace(), null, null, null, null, null,
        null));
  }

  public List<V1Pod> getPodsWithPrefixSortedByCreation(String podNamePrefix) {
    var podList = getCurrentNamespacePods();
    return podList.getItems().stream()
        .filter(pod -> pod.getMetadata().getName().startsWith(podNamePrefix))
        .sorted(Comparator.comparing(pod -> ((V1Pod) pod).getMetadata().getCreationTimestamp()).reversed())
        .toList();
  }

  public V1Job createJob(String jobDefinitionAsYaml) throws IOException {
    V1Job kubeBenchJob = (V1Job) Yaml.load(jobDefinitionAsYaml);
    return apiCall(() -> batchApi.createNamespacedJob(getCurrentNamespace(), kubeBenchJob, null, null, null, null));
  }

  public V1Job createJobFromYamlWithServiceAccount(String jobDefinitionAsYaml, String serviceAccountName) throws IOException {
    V1Job kubeBenchJob = (V1Job) Yaml.load(jobDefinitionAsYaml);
    kubeBenchJob.getSpec().getTemplate().getSpec().setServiceAccountName(serviceAccountName);
    return apiCall(() -> batchApi.createNamespacedJob(getCurrentNamespace(), kubeBenchJob, null, null, null, null));
  }

  public V1Job createJobFromYamlUrlWithModifiedCommand(String yamlUrl, String command) {
    try {
      V1Job kubeBenchJob = (V1Job) Yaml.load(getFileFromUrl(yamlUrl));
      kubeBenchJob.getSpec().getTemplate().getSpec().getContainers().get(0).setCommand(Arrays.stream(command.split(" ")).toList());
      return apiCall(() -> batchApi.createNamespacedJob(getCurrentNamespace(), kubeBenchJob, null, null, null, null));
    } catch (IOException ioException) {
      log.error("Could not obtain kube-bench job definition from URL: {}", yamlUrl, ioException);
      throw new RuntimeException();
    }
  }

  public V1Job createJobFromYamlUrl(String yamlUrl) {
    try {
      V1Job kubeBenchJob = (V1Job) Yaml.load(getFileFromUrl(yamlUrl));
      return apiCall(() -> batchApi.createNamespacedJob(getCurrentNamespace(), kubeBenchJob, null, null, null, null));
    } catch (IOException ioException) {
      log.error("Could not obtain kube-bench job definition from URL: {}", yamlUrl, ioException);
      throw new RuntimeException();
    }
  }

  public InputStream streamPodLogs(String name) {
    return apiCall(() -> podLogs.streamNamespacedPodLog(getCurrentNamespace(), name, null));
  }

  public V1Job createJobFromDefinitionWithContainerZeroArgs(String yaml, List<String> args) {
    try {
      V1Job job = (V1Job) Yaml.load(yaml);
      job.getSpec().getTemplate().getSpec().getContainers().get(0).setArgs(args);
      return apiCall(() -> batchApi.createNamespacedJob(getCurrentNamespace(), job, null, null, null, null));
    } catch (IOException ioException) {
      log.error("Could not obtain job definition from URL: {}", yaml, ioException);
      throw new RuntimeException();
    }
  }

  public V1Job createJobWithContainerZeroArgs(String yamlUrl, List<String> args) {
    try {
      V1Job job = (V1Job) Yaml.load(getFileFromUrl(yamlUrl));
      job.getSpec().getTemplate().getSpec().getContainers().get(0).setArgs(args);
      return apiCall(() -> batchApi.createNamespacedJob(getCurrentNamespace(), job, null, null, null, null));
    } catch (IOException ioException) {
      log.error("Could not obtain job definition from URL: {}", yamlUrl, ioException);
      throw new RuntimeException();
    }
  }

  @Nullable
  private V1PodList getPods(String namespace) {
    return apiCall(
        () -> coreApi.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null, null, null));
  }

  @Nullable
  private V1PodList getCurrentNamespacePods() {
    return apiCall(
        () -> coreApi.listNamespacedPod(getCurrentNamespace(), null, null, null, null, null, null, null, null, null, null, null));
  }
}
