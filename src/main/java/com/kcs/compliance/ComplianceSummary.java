package com.kcs.compliance;

import java.util.function.BinaryOperator;

public record ComplianceSummary(Integer failedChecks, Integer passedChecks, Double score) {

  public static BinaryOperator<ComplianceSummary> accumulator() {
    return (complianceSummary, complianceSummary2) -> {
      var failedSum = complianceSummary.failedChecks + complianceSummary2.failedChecks;
      var passedSum = complianceSummary.passedChecks + complianceSummary2.passedChecks;
      var score = passedSum * 100.0 / (passedSum + failedSum);
      return new ComplianceSummary(
          complianceSummary.failedChecks + complianceSummary2.failedChecks,
          complianceSummary.passedChecks + complianceSummary2.passedChecks,
          score);
    };
  }
}
