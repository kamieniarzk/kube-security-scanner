package com.kcs.bench;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class KubeBenchResultDto {

  private List<CheckCategoryDto> categories;

  @Data
  static class CheckCategoryDto {
    private String identifier;
    private String title;
    private List<CheckGroupDto> groups;

    public CheckCategoryDto(String identifier, String title) {
      this.identifier = identifier;
      this.title = title;
      this.groups = new ArrayList<>();
    }
  }

  @Data
  static class CheckGroupDto {
    private String identifier;
    private String title;
    private List<CheckDto> checks;

    public CheckGroupDto(String identifier, String title) {
      this.identifier = identifier;
      this.title = title;
      this.checks = new ArrayList<>();
    }
  }

  @Data
  static class CheckDto {
    private String identifier;
    private String severity;
    private String title;
    private String remediation;

    public CheckDto(String identifier, String severity, String title) {
      this.identifier = identifier;
      this.severity = severity;
      this.title = title;
    }
  }
}
