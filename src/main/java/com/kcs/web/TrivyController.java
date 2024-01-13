package com.kcs.web;

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

  @GetMapping("/{id}}/result")
  ScanResult getResult(@PathVariable String id) {
    return trivyFacade.getResult(id);
  }
}
