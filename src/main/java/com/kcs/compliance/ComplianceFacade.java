package com.kcs.compliance;

import com.kcs.NoDataFoundException;
import com.kcs.kubescape.KubescapeFacade;
import com.kcs.kubescape.KubescapeResult;
import com.kcs.kubescape.KubescapeRunRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ComplianceFacade {

  private final ComplianceRunRepository runRepository;
  private final KubescapeFacade kubescapeFacade;
  private final ComplianceByNamespaceCalculator<KubescapeResult> calculator;

  public ComplianceScanRun runComplianceScan(ComplianceRunRequest runRequest) {
    var kubescapeRunRequest = new KubescapeRunRequest(Set.of(runRequest.framework()), null);
    var kubescapeComplianceRun = kubescapeFacade.run(kubescapeRunRequest);
    return runRepository.save(new ComplianceScanRun(kubescapeComplianceRun));
  }

  public List<ComplianceScanRun> getAllRuns() {
    return runRepository.findAll();
  }

  public ComplianceByNamespaceSummary getComplianceScanResult(String runId) {
    var run = runRepository.findById(runId).orElseThrow(NoDataFoundException::new);
    var kubescapeResult = kubescapeFacade.getFullDomainResult(run.kubescapeRunId());
    return calculator.calculate(run.framework().getName(), kubescapeResult);
  }
}
