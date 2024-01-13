package com.kcs.kubescore;

import io.kubernetes.client.common.KubernetesObject;

import java.util.List;

public interface YamlStorage {
  String saveAsYamlInTempLocation(List<KubernetesObject> objects, String namespace);
  String saveAsYamlInTempLocation(List<KubernetesObject> objects);
}
