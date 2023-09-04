package com.kcs.hunter.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class KubeHunterResultDto {

  private List<NodeDto> nodes;
  private List<ServiceDto> services;
  private List<VulnerabilityDto> vulnerabilities;

  @Data
  static class ServiceDto {
    private String service;
    private String location;
  }

  @Data
  static class NodeDto {
    private String type;
    private String location;
  }

  @Data
  static class VulnerabilityDto {
    private String location;
    private String vid;
    private String category;
    private String severity;
    private String vulnerability;
    private String description;
    private String evidence;
    @JsonProperty("avd_reference")
    private String avdReference;
    private String hunter;
  }
}
