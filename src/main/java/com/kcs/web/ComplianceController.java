package com.kcs.web;

import com.kcs.compliance.ComplianceByNamespaceSummary;
import com.kcs.compliance.ComplianceFacade;
import com.kcs.compliance.ComplianceScanRequest;
import com.kcs.compliance.ComplianceScanRun;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliance")
@RequiredArgsConstructor
class ComplianceController {

  private final ComplianceFacade complianceFacade;

  @PostMapping("/runs")
  public ComplianceScanRun runComplianceScan(@RequestBody ComplianceScanRequest runRequest) {
    return complianceFacade.runComplianceScan(runRequest);
  }

  @GetMapping("/runs")
  public List<ComplianceScanRun> getAllComplianceRuns() {
    return complianceFacade.getAllRuns();
  }

  @GetMapping(path = "/runs/{id}/result", produces = {"application/json", "text/csv"})
  public ComplianceByNamespaceSummary getComplianceRunResult(@PathVariable String id) {
    return complianceFacade.getComplianceScanResult(id);
  }
}
