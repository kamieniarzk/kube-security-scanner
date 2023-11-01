package com.kcs.trivy;

import com.kcs.trivy.persistence.TrivyRunDto;

interface TrivyRunner {
  TrivyRunDto run();
}
