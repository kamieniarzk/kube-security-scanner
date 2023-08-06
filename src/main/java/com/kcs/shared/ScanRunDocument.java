package com.kcs.shared;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Document("scans")
@AllArgsConstructor
class ScanRunDocument {
  private @Id String id;
  private LocalDateTime date;
  private ScanType type;
  private String podName;
  private Boolean logsStored;

  ScanRun toScanRun() {
    return new ScanRun(this.id, date, type, podName, logsStored);
  }
}
