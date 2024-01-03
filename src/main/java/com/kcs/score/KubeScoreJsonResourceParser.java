package com.kcs.score;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class KubeScoreJsonResourceParser {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static List<KubeScoreJsonResultDto> parseFullResult(String input) {
    try {
      var rawParsed = Arrays.asList(objectMapper.readValue(input, KubeScoreJsonResultDto[].class));
      return filterOutSkippedChecks(rawParsed);
    } catch (JsonProcessingException exception) {
      log.error("Failed to parse trivy JSON result", exception);
      throw new RuntimeException(exception);
    }
  }

  private static List<KubeScoreJsonResultDto> filterOutSkippedChecks(List<KubeScoreJsonResultDto> raw) {
    return raw.stream()
        .map(KubeScoreJsonResourceParser::filterChecksFromResource)
        .toList();
  }

  @NotNull
  private static KubeScoreJsonResultDto filterChecksFromResource(KubeScoreJsonResultDto resource) {
    var checks = resource.getChecks();
    var filteredChecks = checks.stream().filter(check -> !check.getSkipped() && check.getComments() != null).toList();
    resource.setChecks(filteredChecks);
    return resource;
  }
}
