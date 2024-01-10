package com.kcs.kubescape;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
final class KubescapeControlDictionary {
  private final Map<String, KubescapeControls.Control> dictionary = new HashMap<>();

  KubescapeControlDictionary() {
    KubescapeJsonResultParser.parseControls(getControlsJson()).getControls()
        .forEach(control -> dictionary.put(control.getControlID(), control));
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
}
