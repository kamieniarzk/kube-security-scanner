package com.kcs.trivy;

interface TrivyRunner {
  TrivyRunDto run(TrivyRunRequest trivyRunRequest);
}
