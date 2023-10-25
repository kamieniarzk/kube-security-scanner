package com.kcs.context;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ContextHolder {
  private final ClusterType clusterType;
  private final String clusterName;
  private final String helmReleaseName;

  ContextHolder(ClusterTypeResolver clusterTypeResolver, ClusterNameResolver clusterNameResolver, @Value("${HELM_RELEASE_NAME}") String helmReleaseName) {
    this.clusterType = clusterTypeResolver.resolveCurrentClusterType();
    this.clusterName = clusterNameResolver.resolveClusterName();
    this.helmReleaseName = helmReleaseName;
  }
}
