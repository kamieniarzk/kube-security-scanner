package com.kcs.trivy.persistence;

import java.time.LocalDateTime;

final class DocumentFactory {
  static TrivyRunDocument create(TrivyRunCreate runCreate) {
    return new TrivyRunDocument(null, LocalDateTime.now(), runCreate.jobRunId(), false);
  }
}
