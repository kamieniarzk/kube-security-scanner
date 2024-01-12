package com.kcs.compliance;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Compliance {
  private int passedChecks;
  private int failedChecks;

  public Double getScore() {
    return passedChecks * 100.0 / (passedChecks + failedChecks);
  }

  Compliance() {
    this(0, 0);
  }

  void pass() {
    passedChecks += 1;
  }

  void fail() {
    failedChecks += 1;
  }
}
