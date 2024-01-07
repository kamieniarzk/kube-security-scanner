package com.kcs.workload.persistence;

import com.kcs.workload.ScanRunStatus;

import java.time.LocalDateTime;

final class DocumentFactory {
  static Document create(String scoreRunId, String trivyRunId) {
    return new Document(null, LocalDateTime.now(), scoreRunId, trivyRunId, ScanRunStatus.IN_PROGRESS);
  }
}
