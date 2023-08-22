package com.kcs.bench.persistence.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Document("kubeBench")
@AllArgsConstructor
class KubeBenchRunDocument {
  private @Id String id;
  private LocalDateTime date;
  private String podName;
  private Boolean logsStored;

  KubeBenchRun toScanRun() {
    return new KubeBenchRun(this.id, date, podName, logsStored);
  }
}
