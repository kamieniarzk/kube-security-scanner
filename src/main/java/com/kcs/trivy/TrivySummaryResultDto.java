package com.kcs.trivy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
public class TrivySummaryResultDto {

  private String clusterName;
  private List<Resource> resources;

  @Data
  public static class Resource {
    private String namespace;
    private String kind;
    private String name;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Result> results;

    @Nullable
    public Result getResult() {
      if (results != null && results.size() == 1) {
        return results.get(0);
      }
      return null;
    }
  }

  @Data
  public static class Result {
    @JsonProperty("MisconfSummary")
    private Summary summary;
  }

  @Data
  public static class Summary {
    private Integer successes;
    private Integer failures;
    private Integer exceptions;
  }
}
