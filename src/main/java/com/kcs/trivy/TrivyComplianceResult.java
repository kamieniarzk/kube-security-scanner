package com.kcs.trivy;


import lombok.Data;

import java.util.List;

@Data
public class TrivyComplianceResult {

  @Data
  public static class Result {
    private String ID;
    private String name;
    private String description;
    private String severity;
    private List<TrivyFullResultDto.Result> results;
  }
}
