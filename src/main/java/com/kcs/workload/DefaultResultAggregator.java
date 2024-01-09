package com.kcs.workload;

import com.kcs.NoDataFoundException;
import com.kcs.score.KubeScoreFacade;
import com.kcs.score.KubeScoreJsonResultDto;
import com.kcs.trivy.TrivyFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
class DefaultResultAggregator implements ResultAggregator {

  private final AggregatedScanRepository runRepository;
  private final KubeScoreFacade scoreFacade;
  private final TrivyFacade trivyFacade;
  private final ResultMapper<List<KubeScoreJsonResultDto>> scoreResultMapper;

  @Override
  public WorkloadScanResult get(String runId) {
    var runDto = runRepository.findById(runId).orElseThrow(NoDataFoundException::new);
    var scoreResult = scoreFacade.getResult(runDto.getScoreRunId());
    var trivyResult = trivyFacade.getResult(runDto.getTrivyRunId());

    var scoreResultMapped = scoreResultMapper.map(scoreResult);
    return aggregate(trivyResult, scoreResultMapped);
  }

  private static WorkloadScanResult aggregate(WorkloadScanResult result1, WorkloadScanResult result2) {
    var map1 = result1.namespacedResources();
    var map2 = result2.namespacedResources();

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

    var aggregatedByNamespace = new WorkloadScanResult(map1Enriched);
    return aggregateByResource(aggregatedByNamespace);
  }

  private static void enrichMap1WithNamespacesFromMap2(Map.Entry<String, List<K8sResource>> entry, Map<String, List<K8sResource>> map1) {
    if (!map1.containsKey(entry.getKey())) {
      map1.put(entry.getKey(), entry.getValue());
    }
  }

  private static WorkloadScanResult aggregateByResource(WorkloadScanResult result) {
    var resourcesMap =  result.namespacedResources().entrySet().stream().map(entry -> {
      entry.setValue(findDuplicatesInEachList(entry.getValue()));
      return entry;
    }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return new WorkloadScanResult(resourcesMap);
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
