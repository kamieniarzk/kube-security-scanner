package com.kcs.score;

import com.kcs.k8s.K8sResourceType;

public interface KubeScoreService {
  String score(String name, String namespace, K8sResourceType resourceType);
}
