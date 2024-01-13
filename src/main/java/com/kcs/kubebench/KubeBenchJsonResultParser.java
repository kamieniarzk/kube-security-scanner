package com.kcs.kubebench;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class KubeBenchJsonResultParser {
  private KubeBenchJsonResultParser() {
  }

  public static KubeBenchJsonResultDto parse(String input) {
    var objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(input, KubeBenchJsonResultDto.class);
    } catch (JsonProcessingException exception) {
      log.error("Failed to parse kube-bench JSON result", exception);
      throw new RuntimeException(exception);
    }
  }
}
