package com.kcs.bench.persistence.dto;

import java.time.LocalDateTime;

class ScanRunDocumentFactory {
  static KubeBenchRunDocument createDocument(KubeBenchRunCreate kubeBenchRunCreate) {
    return new KubeBenchRunDocument(null, LocalDateTime.now(), kubeBenchRunCreate.podName(), false);
  }
}
