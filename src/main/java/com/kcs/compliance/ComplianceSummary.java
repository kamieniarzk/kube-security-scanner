package com.kcs.compliance;

public record ComplianceSummary(int failedChecks, int passedChecks, int failedResources, int passedResources, double score) {}
