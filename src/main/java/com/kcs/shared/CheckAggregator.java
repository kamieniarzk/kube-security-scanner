package com.kcs.shared;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CheckAggregator {
  public static List<CheckSummary> aggregateChecks(ScanResult scanResult) {
    var allNamespacedChecks = scanResult.getNamespacedResources().values().stream().flatMap(Collection::stream).map(KubernetesResource::getChecks).flatMap(Collection::stream);
//    var allNonNamespacedChecks = scanResult.getNonNamespacedResources().stream().map(KubernetesResource::getChecks).flatMap(Collection::stream);
    var allChecks = allNamespacedChecks.toList();

    var checkMap = new HashMap<String, CheckSummary>();

    for (var check : allChecks) {
      if (checkMap.containsKey(check.originId())) {
        checkMap.get(check.originId()).incrementCount(check);
      } else {
        checkMap.put(check.originId(), CheckSummary.from(check));
      }
    }

    return checkMap.values().stream().sorted().toList();
  }

  private static Map<CheckCategory, CheckSummary> checkCountMap(ScanResult scanResult) {
    var aggregatedChecks = aggregateChecks(scanResult);
    return aggregatedChecks.stream()
        .collect(Collectors.groupingBy(CheckSummary::getCategory)).entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, CheckAggregator::reduce));
  }

  @NotNull
  private static CheckSummary reduce(Map.Entry<CheckCategory, List<CheckSummary>> entry) {
    return entry.getValue().stream().reduce((summary1, summary2) -> CheckSummary.from(summary1.getCategory(), summary1.getCount() + summary2.getCount(), summary1.getLowCount() + summary2.getLowCount(), summary1.getMediumCount() + summary2.getMediumCount(), summary1.getHighCount() + summary2.getHighCount(), summary1.getCriticalCount() + summary2.getCriticalCount(), summary1.getUnknownCount() + summary2.getUnknownCount())).orElse(CheckSummary.from(null, null, null, null, null, null, null));
  }

  public static List<CheckSummary> getSummary(ScanResult scanResult) {
    return checkCountMap(scanResult).values().stream().toList();
  }
}
