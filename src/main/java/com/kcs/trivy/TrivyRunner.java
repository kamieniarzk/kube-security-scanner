package com.kcs.trivy;

interface TrivyRunner {
  TrivyScanDto run(TrivyRunRequest trivyRunRequest);
}
