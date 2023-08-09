package com.kcs.score;

import org.springframework.stereotype.Service;

import com.kcs.k8s.K8sResourceType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class OnHostBinaryKubeScoreService implements KubeScoreService {

  @Override
  public String score(String name, String namespace, K8sResourceType resourceType) {
    return null;
  }
}
