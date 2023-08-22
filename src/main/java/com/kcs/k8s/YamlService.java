package com.kcs.k8s;

import java.util.List;

import io.kubernetes.client.common.KubernetesObject;

public interface YamlService {
  List<String> saveAsYamlInTempLocation(List<KubernetesObject> objects, String namespace);
}
