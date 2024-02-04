package com.kcs.shared;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckSummary implements Comparable<CheckSummary> {
  private final CheckCategory category;
  private final String origin;
  private final String originId;
  private final String title;
  private Long count;

  static CheckSummary from(Check check) {
    var category = CheckCategory.matchCategory(check.originId());
    return new CheckSummary(category, check.origin(), check.originId(), check.title(), 1L);
  }

  static CheckSummary from(CheckCategory category, Long count) {
    return new CheckSummary(category, null, null, null, count);
  }

  public void incrementCount() {
    this.count += 1;
  }

  @Override
  public int compareTo(@NotNull CheckSummary checkSummary) {
    return this.category.compareTo(checkSummary.getCategory());
  }
}
