package com.kcs.workload;

public record Check(Severity severity, String title, String description, String remediation, String origin, String originId, boolean passed) {
  public Check(Severity severity, String title, String description, String remediation, String origin, String originId) {
    this(severity, title, description, remediation, origin, originId, false);
  }
}
