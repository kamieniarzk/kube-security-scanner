package com.kcs.kubescape;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

@Slf4j
final class KubescapeJsonResultParser {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static KubescapeControls parseControls(String input) {
    try {
      return objectMapper.readValue(input, KubescapeControls.class);
    } catch (JsonProcessingException exception) {
      log.error("Failed to parse kubescape JSON result", exception);
      throw new RuntimeException(exception);
    }
  }

  public static KubescapeResult parseFullResult(String input) {
    try {
      var rawParsed = objectMapper.readValue(input, KubescapeResult.class);
      return filterOutPassedControls(rawParsed);
    } catch (JsonProcessingException exception) {
      log.error("Failed to parse kubescape JSON result", exception);
      throw new RuntimeException(exception);
    }
  }

  private static KubescapeResult filterOutPassedControls(KubescapeResult rawParsed) {
    var filteredResults = rawParsed.getResults().stream()
        .map(KubescapeJsonResultParser::filterOutPassedControls)
        .filter(result -> !result.getControls().isEmpty())
        .collect(Collectors.toList());
    rawParsed.setResults(filteredResults);
    return rawParsed;
  }

  @NotNull
  private static KubescapeResult.Result filterOutPassedControls(KubescapeResult.Result result) {
    var filteredControls = result.getControls().stream().filter(control -> "failed".equals(control.getStatus().getStatus())).collect(Collectors.toList());
    result.setControls(filteredControls);
    return result;
  }
}
