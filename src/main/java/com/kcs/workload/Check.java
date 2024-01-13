package com.kcs.workload;

public record Check(Severity severity, String title, String description, String remediation, String origin, String originId, boolean passed, boolean skipped, String skipReason) {
  public Check(Severity severity, String title, String description, String remediation, String origin, String originId) {
    this(severity, title, description, remediation, origin, originId, false, false, null);
  }

  public Check(Severity severity, String title, String description, String remediation, String origin, String originId, boolean passed) {
    this(severity, title, description, remediation, origin, originId, passed, false, null);
  }
}
