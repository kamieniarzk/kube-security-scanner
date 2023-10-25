package com.kcs.trivy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
final class TrivyJobDefinition {
  private TrivyJobDefinition() {}
  static String get() {
    var resource = new ClassPathResource("jobs/trivy-job.yaml");
    try (var inputStream = resource.getInputStream(); var reader = new BufferedReader(new InputStreamReader(inputStream))) {
      return reader.lines().collect(Collectors.joining("\n"));
    } catch (Exception e) {
      log.error("Error while reading trivy job definition", e);
      throw new RuntimeException(e);
    }
  }
}
