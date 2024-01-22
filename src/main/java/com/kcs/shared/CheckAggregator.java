package com.kcs.shared;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CheckAggregator {
  public static List<CheckSummary> aggregateChecks(ScanResult scanResult) {
    var allNamespacedChecks = scanResult.getNamespacedResources().values().stream().flatMap(Collection::stream).map(KubernetesResource::getChecks).flatMap(Collection::stream);
    var allNonNamespacedChecks = scanResult.getNonNamespacedResources().stream().map(KubernetesResource::getChecks).flatMap(Collection::stream);
    var allChecks = Stream.concat(allNamespacedChecks, allNonNamespacedChecks).toList();

    var checkMap = new HashMap<String, CheckSummary>();

    for (var check : allChecks) {
      if (checkMap.containsKey(check.originId())) {
        checkMap.get(check.originId()).incrementCount();
      } else {
        checkMap.put(check.originId(), CheckSummary.from(check));
      }
    }

    return checkMap.values().stream().toList();
  }
}
