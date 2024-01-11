package com.kcs.compliance;

import com.kcs.kubescape.KubescapeResult;
import com.kcs.workload.K8sResource;
import com.kcs.workload.ResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class KubescapeComplianceByNamespaceCalculator implements ComplianceByNamespaceCalculator<KubescapeResult> {

  private final ResultMapper<KubescapeResult> resultMapper;

  @Override
  public ComplianceByNamespaceSummary calculate(String framework, KubescapeResult kubescapeResult) {
    var resultMapped = resultMapper.map(kubescapeResult);
    var byNamespace = resultMapped.namespacedResources().entrySet().stream()
        .map(entry -> Map.entry(entry.getKey(), map(entry.getValue())))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    return new ComplianceByNamespaceSummary(framework, byNamespace, calculateGlobal(byNamespace));
  }

  private static ComplianceSummary calculateGlobal(Map<String, ComplianceSummary> namespacesMap) {
    return namespacesMap.values().stream()
        .reduce(ComplianceSummary.accumulator())
        .orElse(null);
  }

  private static ComplianceSummary map(List<K8sResource> resources) {
    final var failed = new AtomicInteger(0);
    final var passed = new AtomicInteger(0);

    resources.forEach(resource -> resource.getVulnerabilities().forEach(vulnerability -> {
      if (vulnerability.passed()) {
        passed.incrementAndGet();
      } else {
        failed.incrementAndGet();
      }
    }));

    return new ComplianceSummary(failed.get(), passed.get(), passed.get() * 100.0 / (failed.get() + passed.get()));
  }
}
