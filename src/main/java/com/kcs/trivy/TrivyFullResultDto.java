package com.kcs.trivy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TrivyFullResultDto {
  private String clusterName;
  private List<Resource> resources;

  @Data
  public static class Resource {
    private String namespace;
    private String kind;
    private String name;
    private List<Result> results;
  }

  @Data
  public static class Result {
    private Summary misconfSummary;
    private List<Misconfiguration> misconfigurations;
  }

  @Data
  public static class Summary {
    private Integer successes;
    private Integer failures;
    private Integer exceptions;
  }

  @Data
  public static class Misconfiguration {
    private String type;
    @JsonProperty("ID")
    private String id;
    @JsonProperty("AVDID")
    private String avdid;
    private String title;
    private String description;
    private String message;
    private String namespace;
    private String query;
    private String resolution;
    private String severity;
    @JsonProperty("PrimaryURL")
    private String primaryUrl;
    private List<String> references;
    private String status;
    private Cause causeMetadata;
  }

  @Data
  public static class Cause {
    private String provider;
    private Integer startLine;
    private Integer endLine;
  }
}
