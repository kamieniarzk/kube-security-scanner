package com.kcs.trivy;

import java.util.Set;

public record TrivyRunRequest(Set<TrivyScanner> scanners, TrivyCompliance compliance, Set<TrivySeverity> severityFilter, String additionalFlags) {
  public TrivyRunRequest() {
    this(null, null, null, null);
  }
}
