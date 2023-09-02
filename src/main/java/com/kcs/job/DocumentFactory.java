package com.kcs.job;

import java.time.LocalDateTime;

class DocumentFactory {
  static JobRunDocument create(JobRunCreate runCreate) {
    return new JobRunDocument(null, LocalDateTime.now(), runCreate.podName());
  }
}
