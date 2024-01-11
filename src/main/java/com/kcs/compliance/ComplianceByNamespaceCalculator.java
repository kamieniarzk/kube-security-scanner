package com.kcs.compliance;

interface ComplianceByNamespaceCalculator<Source> {
  ComplianceByNamespaceSummary calculate(String framework, Source source);
}
