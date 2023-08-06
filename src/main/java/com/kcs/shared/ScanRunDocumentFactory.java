package com.kcs.shared;

import java.time.LocalDateTime;

class ScanRunDocumentFactory {
  static ScanRunDocument createDocument(ScanRunCreate scanRunCreate) {
    return new ScanRunDocument(null, LocalDateTime.now(), scanRunCreate.type(), scanRunCreate.podName(), false);
  }
}
