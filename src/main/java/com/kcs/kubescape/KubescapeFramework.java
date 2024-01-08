package com.kcs.kubescape;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KubescapeFramework {
  @JsonProperty("allcontrols")
  AllControls("allcontrols"),
  @JsonProperty("armobest")
  ArmoBest("armobest"),
  @JsonProperty("devopsbest")
  DevOpsBest("deveopsbest"),
  @JsonProperty("mitre")
  MITRE("mitre"),
  @JsonProperty("nsa")
  NSA("nsa"),
  @JsonProperty("soc2")
  SOC2("soc2"),
  @JsonProperty("cis_aks")
  cis_aks("cis-aks-t1.2.0"),
  @JsonProperty("cis-eks")
  cis_eks("cis-eks-t1.2.0"),
  @JsonProperty("cis-generic")
  cis_generic("cis-v1.23-t1.0.1");

  private final String name;
}
