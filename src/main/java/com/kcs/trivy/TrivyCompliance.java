package com.kcs.trivy;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrivyCompliance {

  K8S_NSA("k8s-nsa"),
  K8S_CIS("k8s-cis"),
  K8S_PSS_BASELINE("k8s-pss-baseline"),
  K8S_PSS_RESTRICTED("k8s-pss-restricted");

  @JsonValue
  private final String name;
}
