package com.kcs.compliance;

import com.kcs.workload.Check;

import java.util.List;
import java.util.Map;

public record ComplianceByNamespaceSummary(String framework, Map<String, ComplianceSummary> summaryMap, ComplianceSummary globalSummary, List<Check> skippedChecks) {
}
