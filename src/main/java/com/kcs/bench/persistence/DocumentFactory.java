package com.kcs.bench.persistence;

import java.time.LocalDateTime;

class DocumentFactory {
  static KubeBenchRunDocument create(KubeBenchRunCreate kubeBenchRunCreate) {
    return new KubeBenchRunDocument(null, LocalDateTime.now(), kubeBenchRunCreate.jobRunId(), false);
  }
}
