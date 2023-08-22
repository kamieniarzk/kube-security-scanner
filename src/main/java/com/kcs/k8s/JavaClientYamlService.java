package com.kcs.k8s;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import io.kubernetes.client.common.KubernetesObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class JavaClientYamlService implements YamlService {

  private final String tempYamlLocation;

  JavaClientYamlService(@Value("${filesystem.yaml-location:/tmp/kube-config-scanner/yaml}") String tempYamlLocation) {
    this.tempYamlLocation = tempYamlLocation;
  }

  @Override
  public List<String> saveAsYamlInTempLocation(List<KubernetesObject> objects, String namespace) {
    var yamlPath = Paths.get(tempYamlLocation, namespace);
    prepareDirectory(yamlPath);
    return objects.stream().map(object -> this.saveObjectYaml(object, yamlPath)).toList();
  }

  private String saveObjectYaml(final KubernetesObject object, Path path) {
    var objectName = object.getMetadata().getName();
    var fileName = objectName + ".yaml";
    var objectYaml = YamlConverter.convert(object);
    try {
      var savedFilePath = Paths.get(path.toString(), fileName);
      Files.writeString(savedFilePath, objectYaml);
      return savedFilePath.toString();
    } catch (IOException ioException) {
      log.error("Failed to save object {} as yaml", objectName, ioException);
      throw new RuntimeException();
    }
  }

  private static void prepareDirectory(final Path yamlPath) {
    if (Files.exists(yamlPath)) {
      purgeDirectory(new File(yamlPath.toUri()));
      return;
    }

    try {
      Files.createDirectories(yamlPath);
    } catch (IOException ioException) {
      log.error("Failed to create namespaced directory for temporary yaml storage", ioException);
      throw new RuntimeException();
    }
  }

  private static void purgeDirectory(@NonNull File dir) {
    if (!dir.getPath().startsWith("/tmp")) {
      throw new IllegalArgumentException("Can not delete directories other than from temp location");
    }

    for (File file : dir.listFiles()) {
      if (file.isDirectory()) {
        purgeDirectory(file);
      }
      file.delete();
    }
  }
}
