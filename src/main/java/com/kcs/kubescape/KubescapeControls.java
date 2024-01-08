package com.kcs.kubescape;

import lombok.Data;

import java.util.List;

@Data
class KubescapeControls {

  private List<Control> controls;

  @Data
  static class Control {
    private String controlID;
    private String name;
    private String description;
    private String remediation;
    private Integer baseScore;
  }
}
