package com.kcs.kubescape;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;

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

  private static String getControlsJson() {
    var resource = new ClassPathResource("kubescape/controls.json");
    try (var inputStream = resource.getInputStream(); var reader = new BufferedReader(new InputStreamReader(inputStream))) {
      return reader.lines().collect(Collectors.joining("\n"));
    } catch (Exception e) {
      log.error("Error while reading kubescape controls JSON", e);
      throw new RuntimeException(e);
    }
  }

//  private static List<String> getKubescapeJsonFiles() {
//    try {
//      var resourceDirectory = new ClassPathResource("kubescape");
//      var dir = resourceDirectory.getFile();
//      var jsonFiles = dir.listFiles((dir1, name) -> name.endsWith(".json"));
//      if (jsonFiles == null) {
//        log.error("No JSON files found in kubescape directory");
//        throw new IllegalStateException("No JSON files found in kubescape directory");
//      }
//      var jsonContents = new ArrayList<String>();
//      for (var jsonFile : jsonFiles) {
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile)))) {
//          String content = reader.lines().collect(Collectors.joining("\n"));
//          jsonContents.add(content);
//        } catch (Exception e) {
//          log.error("Error while reading file: " + jsonFile.getName(), e);
//        }
//      }
//      return jsonContents;
//    } catch (Exception e) {
//      log.error("Error while reading kubescape JSON files", e);
//      throw new RuntimeException(e);
//    }
//  }

  private static List<String> getKubescapeJsonFiles() {
    try {
      var resourceDirectory = new ClassPathResource("kubescape");
      var dir = resourceDirectory.getFile();
      var jsonFiles = listJsonFiles(dir);
      return readFiles(jsonFiles);
    } catch (Exception e) {
      log.error("Error while reading kubescape JSON files", e);
      throw new RuntimeException(e);
    }
  }

  private static File[] listJsonFiles(File dir) {
    var jsonFiles = dir.listFiles((dir1, name) -> name.endsWith(".json"));
    if (jsonFiles == null) {
      log.error("No JSON files found in kubescape directory");
      throw new IllegalStateException("No JSON files found in kubescape directory");
    }
    return jsonFiles;
  }

  private static List<String> readFiles(File[] files) {
    return Arrays.stream(files)
        .flatMap(KubescapeControlDictionary::readSingleFile)
        .collect(Collectors.toList());
  }

  private static Stream<String> readSingleFile(File file) {
    try {
      return Stream.of(readFile(file));
    } catch (Exception e) {
      log.error("Error while reading file: " + file.getName(), e);
      throw new IllegalStateException("No JSON files found in kubescape directory");
    }
  }

  private static String readFile(File file) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
      return reader.lines().collect(Collectors.joining("\n"));
    }
  }
}
