package com.kcs.k8s;

import io.kubernetes.client.common.KubernetesObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@SuppressWarnings("ResultOfMethodCallIgnored")
class JavaClientYamlService implements YamlService {

  private static String OBJECT_SEPARATOR = "---\n";

  private final String tempYamlLocation;

  JavaClientYamlService(@Value("${filesystem.yaml-location:/tmp/kube-config-scanner/yaml}") String tempYamlLocation) {
    this.tempYamlLocation = tempYamlLocation;
  }

  @Override
  public String saveAsYamlInTempLocation(List<KubernetesObject> objects, String namespace) {
    var yamlPath = Paths.get(tempYamlLocation, namespace);
    prepareDirectory(yamlPath);
    var allObjectsYaml = objects.stream()
        .map(this::convertObjectToYamlString)
        .collect(Collectors.joining(OBJECT_SEPARATOR));
    return saveIntoFile(allObjectsYaml, namespace, yamlPath);
  }


  private String convertObjectToYamlString(KubernetesObject object) {
    return YamlConverter.convert(object);
  }

  private String saveIntoFile(String yaml, String namespace, Path path) {
    try {
      var savedFilePath = Paths.get(path.toString(), namespace.concat(".yaml"));
      Files.writeString(savedFilePath, yaml);
      return savedFilePath.toString();
    } catch (IOException ioException) {
      log.error("Failed to save {} namespace objects as yaml", namespace, ioException);
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

    var files = dir.listFiles();

    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          purgeDirectory(file);
        }
        file.delete();
      }
    }
  }
}
