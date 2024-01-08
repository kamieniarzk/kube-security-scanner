package com.kcs.kubescape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KubescapeFramework {
  AllControls("allcontrols"),
  ArmoBest("armobest"),
  DevOpsBest("deveopsbest"),
  MITRE("mitre"),
  NSA("nsa"),
  SOC2("soc2"),
  cis_aks("cis-aks-t1.2.0"),
  cis_eks("cis-eks-t1.2.0"),
  cis_generic("cis-v1.23-t1.0.1");
  private final String name;
}
