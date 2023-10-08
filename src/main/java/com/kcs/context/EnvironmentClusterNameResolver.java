package com.kcs.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class EnvironmentClusterNameResolver implements ClusterNameResolver {
  private final String clusterName;

  EnvironmentClusterNameResolver(@Value("${CLUSTER_NAME}") String clusterName) {
    this.clusterName = clusterName;
  }

  @Override
  public String resolveClusterName() {
    return clusterName;
  }
}
