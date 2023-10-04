package com.kcs.context;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class ContextHolder {
  @Getter
  private final ClusterType clusterType;

  ContextHolder(ClusterTypeResolver clusterTypeResolver) {
    this.clusterType = clusterTypeResolver.resolveCurrentClusterType();
  }
}
