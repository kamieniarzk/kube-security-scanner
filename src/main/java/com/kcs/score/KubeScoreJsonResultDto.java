package com.kcs.score;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class KubeScoreJsonResultDto {

  @JsonProperty("type_meta")
  private ResourceTypeMetadata typeMeta;
  @JsonProperty("object_meta")
  private ResourceMetadata objectMeta;
  private List<Check> checks;

  @Data
  static class ResourceTypeMetadata {
    private String kind;
    private String apiVersion;
  }

  @Data
  static class ResourceMetadata {
    private String name;
    private String namespace;
    private List<OwnerReference> ownerReferences;
  }

  @Data
  static class OwnerReference {
    private String kind;
    private String name;
  }

  @Data
  static class Check {
    private CheckData check;
    private Integer grade;
    private Boolean skipped;
    private List<Comment> comments;
  }

  @Data
  static class CheckData {
    private String name;
    private String id;
    private String comment;
  }

  @Data
  static class Comment {
    private String path;
    private String summary;
    private String description;
  }
}
