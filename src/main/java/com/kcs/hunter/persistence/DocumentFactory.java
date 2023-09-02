package com.kcs.hunter.persistence;

import java.time.LocalDateTime;

final class DocumentFactory {
  static KubeHunterRunDocument create(KubeHunterRunCreate runCreate) {
    return new KubeHunterRunDocument(null, LocalDateTime.now(), runCreate.args(), runCreate.jobRunId(), false);
  }
}
