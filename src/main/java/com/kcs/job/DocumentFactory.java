package com.kcs.job;

import java.time.LocalDateTime;

class DocumentFactory {
  static JobDocument create(JobCreate runCreate) {
    return new JobDocument(null, LocalDateTime.now(), runCreate.podName());
  }
}
