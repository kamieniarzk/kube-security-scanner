package com.kcs.kubescape;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class KubescapeResult {

  private List<Resource> resources;
  private List<Result> results;
  private SummaryDetails summaryDetails;

  @Data
  static class Result {
    private String resourceID;
    private List<Control> controls;
  }

  @Data
  static class Control {
    private String controlID;
    private String name;
    private Status status;
  }

  @Data
  static class Status {
    private String status;
  }

  @Data
  static class Resource {
    private String resourceID;
    private ResourceData object;
  }

  @Data
  static class ResourceData {
    private String apiVersion;
    private String kind;
    private String apiGroup;
    private String name;
    private String namespace;
    private ResourceMetadata metadata;

    public String getName() {
      if (name != null) {
        return name;
      } else if (metadata != null) {
        return metadata.name;
      }
      return null;
    }

    public String getNamespace() {
      if (namespace != null) {
        return namespace;
      }
      if (metadata != null) {
        return metadata.namespace;
      }
      return null;
    }
  }

  @Data
  static class ResourceMetadata {
    private String name;
    private String namespace;
  }

  @Data
  static class SummaryDetails {
    private Map<String, ControlSummary> controls;
    @Data
    static class ControlSummary {
      private String controlID;
      private StatusInfo statusInfo;
      @Data
      static class StatusInfo {
        private String status;
        private String info;
      }
    }
  }
}
