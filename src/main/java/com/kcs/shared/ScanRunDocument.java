package com.kcs.shared;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("scans")
record ScanRunDocument(@Id String id, LocalDateTime date, ScanType type, String podName) {
  ScanRun toScanRun() {
    return new ScanRun(id, date, type, podName);
  }
}
