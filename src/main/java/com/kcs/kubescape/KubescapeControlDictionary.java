package com.kcs.kubescape;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
final class KubescapeControlDictionary {
  private final Map<String, KubescapeControls.Control> dictionary = new HashMap<>();

  KubescapeControlDictionary() {
    getKubescapeJsonFiles().forEach(this::parseFile);
  }

  private void parseFile(String controlsFile) {
    KubescapeJsonResultParser.parseControls(controlsFile).getControls()
        .forEach(this::addIfNotPresent);
  }

  private void addIfNotPresent(KubescapeControls.Control control) {
    if (!dictionary.containsKey(control.getControlID())) {
      dictionary.put(control.getControlID(), control);
    }
  }

  KubescapeControls.Control get(String controlID) {
    return dictionary.get(controlID);
  }

  private static List<String> getKubescapeJsonFiles() {
    var files = new ArrayList<String>();
    try {
      var resourcePatResolver = new PathMatchingResourcePatternResolver();
      var allResources = resourcePatResolver.getResources("classpath*:kubescape/*.json");
      for(var resource: allResources) {
        var inputStream = resource.getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
          var string = reader.lines().collect(Collectors.joining("\n"));
          files.add(string);
        }
      }
      return files;
    } catch (Exception e) {
      log.error("Error while reading kubescape JSON files", e);
      throw new RuntimeException(e);
    }
  }
}
