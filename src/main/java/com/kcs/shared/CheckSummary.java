package com.kcs.shared;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckSummary implements Comparable<CheckSummary> {
  private final CheckCategory category;
  private final Severity severity;
  private final String origin;
  private final String originId;
  private final String title;
  private Long count;
  private Long lowCount;
  private Long mediumCount;
  private Long highCount;
  private Long criticalCount;
  private Long unknownCount;

  static CheckSummary from(Check check) {
    var category = CheckCategory.matchCategory(check.originId());
    return new CheckSummary(category, check.severity(), check.origin(), check.originId(), check.title(), 1L,
        check.severity() == Severity.LOW ? 1L : 0L,
        check.severity() == Severity.MEDIUM ? 1L : 0L,
        check.severity() == Severity.HIGH ? 1L : 0L,
        check.severity() == Severity.CRITICAL ? 1L : 0L,
        check.severity() == Severity.UNKNOWN ? 1L : 0L);
  }

  static CheckSummary from(CheckCategory category, Long count, Long lowCount, Long mediumCount, Long highCount, Long criticalCount, Long unknownCount) {
    return new CheckSummary(category, null, null, null, null, count, lowCount, mediumCount, highCount, criticalCount, unknownCount);
  }

  public void incrementCount(Check check) {
    this.count += 1;
    switch (check.severity()) {
      case LOW -> this.lowCount += 1;
      case MEDIUM -> this.mediumCount += 1;
      case HIGH -> this.highCount += 1;
      case CRITICAL -> this.criticalCount += 1;
      case UNKNOWN -> this.unknownCount += 1;
    }
  }

  @Override
  public int compareTo(@NotNull CheckSummary checkSummary) {
    return this.category.compareTo(checkSummary.getCategory());
  }
}
