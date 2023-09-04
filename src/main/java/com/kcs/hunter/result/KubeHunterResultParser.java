package com.kcs.hunter.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KubeHunterResultParser {
  public static KubeHunterResultDto parse(String input) {
    var objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(extractJsonFromInput(input), KubeHunterResultDto.class);
    } catch (JsonProcessingException exception) {
      log.error("Failed to parse kube-hunter result", exception);
      throw new RuntimeException(exception);
    }
  }

  private static String extractJsonFromInput(String input) {
    return input.lines().filter(line -> line.startsWith(JsonToken.START_OBJECT.asString())).findFirst().orElseThrow(RuntimeException::new);
  }
}
