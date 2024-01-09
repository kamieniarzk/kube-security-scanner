package com.kcs.trivy;

import java.util.Set;

public record TrivyRunCreate(String jobRunId, String command, Set<TrivyScanner> scanners, Set<TrivySeverity> severity, TrivyCompliance compliance) {
}
