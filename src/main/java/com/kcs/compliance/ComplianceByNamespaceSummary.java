package com.kcs.compliance;

import java.util.Map;

public record ComplianceByNamespaceSummary(String framework, Map<String, ComplianceSummary> summaryMap, ComplianceSummary globalSummary) {
}
