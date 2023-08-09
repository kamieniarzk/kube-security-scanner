package com.kcs.k8s;

public interface YamlService {
  String getAsYaml(String name, String namespace, K8sResourceType resourceType);
}
