package com.kcs.web;

import com.kcs.shared.ResultSearchParams;
import com.kcs.trivy.TrivyFacade;
import com.kcs.trivy.TrivyScanDto;
import com.kcs.trivy.TrivyRunRequest;
import com.kcs.shared.ScanResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scans/trivy")
@RequiredArgsConstructor
class TrivyController {

  private final TrivyFacade trivyFacade;

  @GetMapping
  List<TrivyScanDto> getAllTrivyRuns() {
    return trivyFacade.getAll();
  }

  @PostMapping
  TrivyScanDto runTrivy(@RequestBody TrivyRunRequest runRequest) {
    return trivyFacade.run(runRequest);
  }

  @GetMapping(path = "/{id}/result", produces = {"application/json", "text/csv"})
  ScanResult getResult(@PathVariable String id, ResultSearchParams searchParams) {
    return trivyFacade.getResult(id, searchParams);
  }

  @GetMapping("/{id}/original-result")
  String getOriginalResult(@PathVariable String id) {
    return trivyFacade.getOriginalResult(id);
  }
}
