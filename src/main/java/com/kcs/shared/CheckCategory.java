package com.kcs.shared;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
public enum CheckCategory {
  IMAGE(Set.of("KSV013", "container-image-tag", "container-image-pull-policy")),
  PORT_BINDING(Set.of("C-0044", "KSV024", "KSV117", "service-type")),
  NETWORK_POLICIES(Set.of("C-0049", "C-0030", "pod-networkpolicy", "C-0054")),
  RESOURCE_LIMITS_AND_REQUESTS(Set.of("C-0050", "KSV015", "KSV016", "KSV011", "container-resources", "KSV018", "C-0004", "C-0009", "container-ephemeral-storage-request-and-limit")),
  PROBES_AND_HEALTHCHECKS(Set.of("C-0056", "C-0018", "pod-probes", "DS026")),
  SECURITY_CONTEXT(Set.of("C-0013", "C-0016", "KSV012", "container-security-context-readonlyrootfilesystem", "C-0046", "KSV003", "KSV001", "container-security-context-user-group-id", "KSV116", "KSV105", "KSV106", "KSV022", "KSV020", "KSV021", "KSV104", "KSV026", "C-0055", "KSV014", "C-0017", "KSV030")),
  RBAC(Set.of("C-0002", "C-0035", "C-0031", "C-0034", "C-0015", "C-0053", "KSV049", "C-0007", "C-0063", "C-0262", "C-0065")),
  SENSITIVE_DATA(Set.of("AVD-KSV-0109", "KSV113", "AVD-KSV-01010", "C-0012")),
  LABELS(Set.of("C-0076", "C-0077")),
  BUGS(Set.of("service-targets-pod", "DS005", "C-0073", "C-0021")),
  UNCATEGORIZED(Collections.emptySet());

  private final Set<String> originIds;

  public static CheckCategory matchCategory(String originId) {
    return Arrays.stream(values())
        .filter(category -> category.originIds.contains(originId))
        .findAny()
        .orElse(UNCATEGORIZED);
  }
}
