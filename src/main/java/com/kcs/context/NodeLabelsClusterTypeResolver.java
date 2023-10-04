package com.kcs.context;

import com.kcs.k8s.KubernetesApiClientWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
class NodeLabelsClusterTypeResolver implements ClusterTypeResolver {

  private static final String INSTANCE_TYPE_LABEL = "beta.kubernetes.io/instance-type";
  private static final Map<String, ClusterType> labelPrefixClusterTypeMap = Map.of(
      "eks.amazonaws.com", ClusterType.EKS,
      "cloud.google.com", ClusterType.GKE
  );
  private static final Map<String, ClusterType> instanceTypeLabelValueMap = Map.of(
      "k3s", ClusterType.K3S,
      "rke2", ClusterType.RKE2
  );
  private final KubernetesApiClientWrapper k8sApi;


  @Override
  public ClusterType resolveCurrentClusterType() {
    var anyNode = this.k8sApi.getAnyNode().orElseThrow(() -> new IllegalStateException("Failed to determine cluster type - could not fetch any node"));
    var matchedByLabelPrefix = anyNode.getMetadata().getLabels().keySet().stream()
        .map(this::matchesClusterType)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findAny();

    if (matchedByLabelPrefix.isPresent()) {
      return matchedByLabelPrefix.get();
    }

    if (anyNode.getMetadata().getLabels().containsKey(INSTANCE_TYPE_LABEL)) {
      var instanceTypeLabelValue = anyNode.getMetadata().getLabels().get(INSTANCE_TYPE_LABEL);
      if (instanceTypeLabelValueMap.containsKey(instanceTypeLabelValue)) {
        return instanceTypeLabelValueMap.get(instanceTypeLabelValue);
      }
    }

    throw new IllegalStateException("Failed to determine cluster type by node labels");
  }

  private Optional<ClusterType> matchesClusterType(String label) {
    return labelPrefixClusterTypeMap.entrySet().stream()
        .filter(entry -> label.startsWith(entry.getKey()))
        .findFirst()
        .map(Map.Entry::getValue);
  }
}
