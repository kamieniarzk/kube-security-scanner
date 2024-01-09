package com.kcs.trivy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrivyScanner {
  @JsonProperty("vuln")
  VULN("vuln"),
  @JsonProperty("config")
  CONFIG("config"),
  @JsonProperty("secret")
  SECRET("secret"),
  @JsonProperty("rbac")
  RBAC("rbac");

  private final String name;
}
