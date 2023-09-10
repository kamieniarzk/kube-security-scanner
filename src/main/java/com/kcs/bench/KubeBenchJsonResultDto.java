package com.kcs.bench;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class KubeBenchJsonResultDto {

  @JsonProperty("Controls")
  private List<ControlDto> controls;
  @JsonProperty("Totals")
  private TotalDto totals;

  @Data
  static class ControlDto {
    private String id;
    private String version;
    @JsonProperty("detected_version")
    private String detectedVersion;
    private String text;
    @JsonProperty("node_type")
    private String nodeType;
    private List<TestDto> tests;
    @JsonProperty("total_pass")
    private Integer totalPass;
    @JsonProperty("total_fail")
    private Integer totalFail;
    @JsonProperty("total_warn")
    private Integer totalWarn;
    @JsonProperty("total_info")
    private Integer totalInfo;
  }

  @Data
  static class TestDto {
    private String section;
    private String type;
    private Integer pass;
    private Integer fail;
    private String warn;
    private Integer info;
    @JsonProperty("desc")
    private String description;
    private List<ResultDto> results;
  }

  @Data
  static class ResultDto {
    @JsonProperty("test_number")
    private String testNumber;
    @JsonProperty("test_desc")
    private String testDesc;
    private String audit;
    @JsonProperty("AuditEnv")
    private String auditEnv;
    @JsonProperty("AuditConfig")
    private String auditConfig;
    private String type;
    private String remediation;
    @JsonProperty("test_info")
    private List<String> testInfo;
    private String status;
    @JsonProperty("actual_value")
    private String actualValue;
    private Boolean scored;
    @JsonProperty("IsMultiple")
    private Boolean isMultiple;
    @JsonProperty("expected_result")
    private String expectedResult;
    private String reason;
  }

  @Data
  static class TotalDto {
    @JsonProperty("total_pass")
    private Integer totalPass;
    @JsonProperty("total_fail")
    private Integer totalFail;
    @JsonProperty("total_warn")
    private Integer totalWarn;
    @JsonProperty("total_info")
    private Integer totalInfo;
  }
}
