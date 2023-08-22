package com.kcs.score.persistence.document;

import java.time.LocalDateTime;

final class DocumentFactory {
  static KubeScoreRunDocument create(KubeScoreRunCreate runCreate) {
    return new KubeScoreRunDocument(null, LocalDateTime.now(), runCreate.namespace());
  }
}
