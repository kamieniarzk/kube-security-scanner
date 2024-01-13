package com.kcs.trivy;

import java.util.Set;

public record TrivyScanCreate(String jobRunId, String command, Set<TrivyScanner> scanners, Set<TrivySeverity> severity, TrivyCompliance compliance) {
}
