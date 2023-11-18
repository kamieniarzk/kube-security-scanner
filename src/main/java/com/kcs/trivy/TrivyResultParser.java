package com.kcs.trivy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
public final class TrivyResultParser {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private TrivyResultParser() {
  }

  public static TrivyFullResultDto parseFullResult(String input) {
    try {
      return objectMapper.readValue(input, TrivyFullResultDto.class);
    } catch (JsonProcessingException exception) {
      log.error("Failed to parse trivy JSON result", exception);
      throw new RuntimeException(exception);
    }
  }

  public static TrivySummaryResultDto parseSummaryResult(String input) {
    try {
      return cleanOutput(objectMapper.readValue(input, TrivySummaryResultDto.class));
    } catch (JsonProcessingException exception) {
      log.error("Failed to parse trivy JSON result", exception);
      throw new RuntimeException(exception);
    }
  }

  private static TrivySummaryResultDto cleanOutput(TrivySummaryResultDto rawOutput) {
    var validResources = rawOutput.getResources().stream().filter(resource -> Objects.nonNull(resource.getResults())).toList();
    rawOutput.setResources(validResources);
    return rawOutput;
  }
}
