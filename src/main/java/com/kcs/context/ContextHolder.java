package com.kcs.context;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class ContextHolder {
  @Getter
  private final ClusterType clusterType;
  @Getter
  private final String clusterName;

  ContextHolder(ClusterTypeResolver clusterTypeResolver, ClusterNameResolver clusterNameResolver) {
    this.clusterType = clusterTypeResolver.resolveCurrentClusterType();
    this.clusterName = clusterNameResolver.resolveClusterName();
  }
}
