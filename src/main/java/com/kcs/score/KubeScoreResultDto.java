package com.kcs.score;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class KubeScoreResultDto {

  private List<ObjectResultDto> objects;

  @Data
  @NoArgsConstructor
  static class ObjectResultDto {
    private String kind;
    private String name;
    private String namespace;
    private List<VulnerabilityTypeDto> vulnerabilities;

    public ObjectResultDto(String kind, String name, String namespace) {
      this.kind = kind;
      this.name = name;
      this.namespace = namespace;
    }
  }

  @Data
  static class VulnerabilityTypeDto {
    private String severity;
    private String title;
    private List<VulnerabilityInstanceDto> instances;

    public VulnerabilityTypeDto(String severity, String title) {
      this.severity = severity;
      this.title = title;
    }
  }

  @Data
  static class VulnerabilityInstanceDto {
    private String title;
    private String remediation;

    public VulnerabilityInstanceDto(String title) {
      this.title = title;
    }
  }
}
