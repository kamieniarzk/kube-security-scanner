package com.kcs.shared;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckSummary {
  private final String origin;
  private final String originId;
  private final String title;
  private Long count;

  static CheckSummary from(Check check) {
    return new CheckSummary(check.origin(), check.originId(), check.title(), 1L);
  }

  public void incrementCount() {
    this.count += 1;
  }
}
