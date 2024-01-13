package com.kcs.compliance;

import com.kcs.kubescape.KubescapeResult;
import com.kcs.workload.Check;
import com.kcs.workload.K8sResource;
import com.kcs.workload.ResultMapper;
import com.kcs.workload.WorkloadScanResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
class KubescapeComplianceByNamespaceCalculator implements ComplianceByNamespaceCalculator<KubescapeResult> {

  private final ResultMapper<KubescapeResult> resultMapper;

  @Override
  public ComplianceByNamespaceSummary calculate(String framework, KubescapeResult kubescapeResult) {
    var resultMapped = resultMapper.map(kubescapeResult);
    return new KubescapeInternalComplianceCalculator().calculate(framework, resultMapped);
  }

  static final class KubescapeInternalComplianceCalculator {

    private final Map<String, Compliance> globalPerCheckMap = new HashMap<>();

    public ComplianceByNamespaceSummary calculate(String framework, WorkloadScanResult resultMapped) {
      var byNamespace = resultMapped.getNamespacedResources().entrySet().stream()
          .map(entry -> Map.entry(entry.getKey(), map(entry.getValue())))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

      return new ComplianceByNamespaceSummary(framework, byNamespace, calculateGlobal(resultMapped), resultMapped.getSkippedChecks());
    }

    private ComplianceSummary calculateGlobal(WorkloadScanResult workloadScanResult) {
      var namespacedResources = workloadScanResult.getNamespacedResources().values().stream().flatMap(Collection::stream);
      var allResources = Stream.concat(namespacedResources, workloadScanResult.getNonNamespacedResources().stream()).toList();
      return calculate(allResources, globalPerCheckMap);
    }

    private ComplianceSummary map(List<K8sResource> resources) {
      var perCheckMap = new HashMap<String, Compliance>();
      resources.forEach(resource -> resource.getChecks().forEach(check -> {
        updateMap(check, perCheckMap);
        updateMap(check, globalPerCheckMap);
      }));
      return calculate(resources, perCheckMap);
    }

    private static ComplianceSummary calculate(List<K8sResource> resources, Map<String, Compliance> perCheckCompliance) {
      var sum = 0.0d;
      for (var controlCompliance : perCheckCompliance.values()) {
        sum += controlCompliance.getScore();
      }
      var checks = perCheckCompliance.size();
      var namespaceScore = sum / checks;
      var failedChecks = perCheckCompliance.values().stream().map(Compliance::getFailedChecks).filter(value -> value > 0).count();
      var passedChecks = checks - failedChecks;
      var failedResources = resources.stream().filter(resource -> resource.getChecks().stream().anyMatch(check -> !check.passed())).count();
      var passedResources = resources.size() - failedResources;
      return new ComplianceSummary((int) failedChecks, (int) passedChecks, (int) failedResources, (int) passedResources, namespaceScore);
    }

    private static void updateMap(Check check, Map<String, Compliance> perCheckMap) {
      var complianceSummary = perCheckMap.getOrDefault(check.originId(), new Compliance());
      if (check.passed()) {
        complianceSummary.pass();
      } else {
        complianceSummary.fail();
      }
      perCheckMap.put(check.originId(), complianceSummary);
    }
  }
}
