package com.kcs.kubebench.persistence;

import java.time.LocalDateTime;

class DocumentFactory {
  static KubeBenchScanDocument create(KubeBenchScanCreate kubeBenchScanCreate) {
    return new KubeBenchScanDocument(null, LocalDateTime.now(), kubeBenchScanCreate.jobRunId(), false, kubeBenchScanCreate.clusterName(), kubeBenchScanCreate.clusterType());
  }
}
