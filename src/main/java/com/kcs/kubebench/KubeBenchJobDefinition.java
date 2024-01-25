package com.kcs.kubebench;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class KubeBenchJobDefinition {
  static String get() {
    var resource = new ClassPathResource("jobs/cloud-bench-job.yaml");
    try (var inputStream = resource.getInputStream(); var reader = new BufferedReader(new InputStreamReader(inputStream))) {
      return reader.lines().collect(Collectors.joining("\n"));
    } catch (Exception e) {
      log.error("Error while reading kube-bench job definition", e);
      throw new RuntimeException(e);
    }
  }

  static String getMaster() {
    var resource = new ClassPathResource("jobs/master-bench-job.yaml");
    try (var inputStream = resource.getInputStream(); var reader = new BufferedReader(new InputStreamReader(inputStream))) {
      return reader.lines().collect(Collectors.joining("\n"));
    } catch (Exception e) {
      log.error("Error while reading kube-bench job definition", e);
      throw new RuntimeException(e);
    }
  }
}
