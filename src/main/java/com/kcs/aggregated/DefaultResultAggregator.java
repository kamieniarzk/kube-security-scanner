package com.kcs.aggregated;

import com.kcs.shared.KubernetesResource;
import com.kcs.shared.ScanResult;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
class DefaultResultAggregator implements ResultAggregator {

  @Override
  public ScanResult aggregate(ScanResult... results) {
    if (results == null || results.length == 0) {
      return new ScanResult();
    }

    if (results.length == 1) {
      return results[0];
    }

    var aggregatedResult = results[0];

    for (int i = 1; i < results.length; i++) {
      aggregatedResult = aggregate(aggregatedResult, results[i]);
    }

    return aggregatedResult;
  }

  private static ScanResult aggregate(ScanResult result1, ScanResult result2) {
    var map1 = result1.getNamespacedResources();
    var map2 = result2.getNamespacedResources();

    var map1Enriched = map1.entrySet().stream()
        .map(entry -> {
          var namespace = entry.getKey();
          var resources = entry.getValue().stream();

          if (map2.containsKey(namespace)) {
            resources = Stream.concat(resources, map2.get(namespace).stream());
          }

          entry.setValue(resources.toList());
          return entry;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    map2.entrySet().
        forEach(entry -> enrichMap1WithNamespacesFromMap2(entry, map1Enriched));

    var skippedChecks = Stream.concat(result1.getSkippedChecks().stream(), result2.getSkippedChecks().stream()).toList();
    var nonNamespacedResources = Stream.concat(result1.getNonNamespacedResources().stream(), result2.getNonNamespacedResources().stream()).toList();
    var nonNamespacedResourcesAggregated = findDuplicatesInEachList(nonNamespacedResources);

    return aggregateByResource(new ScanResult(map1Enriched, nonNamespacedResourcesAggregated, skippedChecks));
  }

  private static void enrichMap1WithNamespacesFromMap2(Map.Entry<String, List<KubernetesResource>> entry, Map<String, List<KubernetesResource>> map1) {
    if (!map1.containsKey(entry.getKey())) {
      map1.put(entry.getKey(), entry.getValue());
    }
  }

  private static ScanResult aggregateByResource(ScanResult result) {
    var resourcesMap =  result.getNamespacedResources().entrySet().stream().map(entry -> {
      entry.setValue(findDuplicatesInEachList(entry.getValue()));
      return entry;
    }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return new ScanResult(resourcesMap, result.getNonNamespacedResources(), result.getSkippedChecks());
  }

  private static List<KubernetesResource> findDuplicatesInEachList(List<KubernetesResource> listOfResources) {
    var setOfResources = new HashSet<KubernetesResource>();
    for (KubernetesResource resource : listOfResources) {
      if (!setOfResources.add(resource)) { // duplicate found
        var vulnerabilitiesOfAllDuplicates = findAllDuplicates(resource, listOfResources).stream()
            .map(KubernetesResource::getChecks)
            .flatMap(Collection::stream)
            .toList();
        setOfResources.stream().filter(res -> res.equals(resource)).findAny().orElseThrow().setChecks(vulnerabilitiesOfAllDuplicates);
      }
    }
    return setOfResources.stream().toList();
  }

  private static List<KubernetesResource> findAllDuplicates(KubernetesResource object, List<KubernetesResource> list) {
    return list.stream()
        .filter(resource -> resource.equals(object))
        .toList();
  }
}
