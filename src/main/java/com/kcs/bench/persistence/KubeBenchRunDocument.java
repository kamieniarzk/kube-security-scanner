package com.kcs.bench.persistence;

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
  private String jobRunId;
  private Boolean logsStored;

  KubeBenchRunDto toDto() {
    return new KubeBenchRunDto(this.id, date, jobRunId, logsStored);
  }
}
