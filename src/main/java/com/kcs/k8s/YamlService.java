package com.kcs.k8s;

import io.kubernetes.client.common.KubernetesObject;

import java.util.List;

public interface YamlService {
  String saveAsYamlInTempLocation(List<KubernetesObject> objects, String namespace);
  String saveAsYamlInTempLocation(List<KubernetesObject> objects);
}
