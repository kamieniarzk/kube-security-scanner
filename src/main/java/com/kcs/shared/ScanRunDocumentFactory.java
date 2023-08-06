package com.kcs.shared;

class ScanRunDocumentFactory {
  static ScanRunDocument createDocument(ScanRun scanRun) {
    return new ScanRunDocument(scanRun.id(), scanRun.date(), scanRun.type(), scanRun.podName());
  }
}
