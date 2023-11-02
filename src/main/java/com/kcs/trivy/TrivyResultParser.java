package com.kcs.trivy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kcs.bench.KubeBenchJsonResultDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TrivyResultParser {
  private TrivyResultParser() {
  }

  public static TrivyResultDto parse(String input) {
    var objectMapper = new ObjectMapper();
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      return objectMapper.readValue(input, TrivyResultDto.class);
    } catch (JsonProcessingException exception) {
      log.error("Failed to parse trivy JSON result", exception);
      throw new RuntimeException(exception);
    }
  }
}
