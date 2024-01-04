package com.kcs.aggregated;

import com.kcs.aggregated.persistence.AggregatedRunRepository;
import com.kcs.score.KubeScoreFacade;
import com.kcs.score.KubeScoreJsonResultDto;
import com.kcs.trivy.TrivyFacade;
import com.kcs.trivy.TrivyFullResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
class DefaultResultAggregator implements ResultAggregator {

  private final AggregatedRunRepository runRepository;
  private final KubeScoreFacade scoreFacade;
  private final TrivyFacade trivyFacade;
  private final ResultMapper<TrivyFullResultDto> trivyResultMapper;
  private final ResultMapper<List<KubeScoreJsonResultDto>> scoreResultMapper;

  @Override
  public AggregatedScanResult get(String runId) {
    var runDto = runRepository.get(runId);
    var scoreResult = scoreFacade.getResult(runDto.scoreRunId());
    var trivyResult = trivyFacade.getResult(runDto.trivyRunId());

    var trivyResultMapped = trivyResultMapper.map(trivyResult);
    var scoreResultMapped = scoreResultMapper.map(scoreResult);
    return aggregate(trivyResultMapped, scoreResultMapped);
  }

  private static AggregatedScanResult aggregate(AggregatedScanResult result1, AggregatedScanResult result2) {
    var map1 = result1.namespaceResourceMap();
    var map2 = result2.namespaceResourceMap();

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

    var aggregatedByNamespace = new AggregatedScanResult(map1Enriched);
    return aggregateByResource(aggregatedByNamespace);
  }

  private static void enrichMap1WithNamespacesFromMap2(Map.Entry<String, List<K8sResource>> entry, Map<String, List<K8sResource>> map1) {
    if (!map1.containsKey(entry.getKey())) {
      map1.put(entry.getKey(), entry.getValue());
    }
  }

  private static AggregatedScanResult aggregateByResource(AggregatedScanResult result) {
    var resourcesMap =  result.namespaceResourceMap().entrySet().stream().map(entry -> {
      entry.setValue(findDuplicatesInEachList(entry.getValue()));
      return entry;
    }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return new AggregatedScanResult(resourcesMap);
  }

  private static List<K8sResource> findDuplicatesInEachList(List<K8sResource> listOfResources) {
    var setOfResources = new HashSet<K8sResource>();
    for (K8sResource resource : listOfResources) {
      if (!setOfResources.add(resource)) { // duplicate found
        var vulnerabilitiesOfAllDuplicates = findAllDuplicates(resource, listOfResources).stream()
            .map(K8sResource::getVulnerabilities)
            .flatMap(Collection::stream)
            .toList();
        setOfResources.stream().filter(res -> res.equals(resource)).findAny().orElseThrow().setVulnerabilities(vulnerabilitiesOfAllDuplicates);
      }
    }
    return setOfResources.stream().toList();
  }

  private static List<K8sResource> findAllDuplicates(K8sResource object, List<K8sResource> list) {
    return list.stream()
        .filter(resource -> resource.equals(object))
        .toList();
  }
}
