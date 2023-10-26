package com.kcs.trivy.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("trivyRun")
@AllArgsConstructor
class TrivyRunDocument {
  private @Id String id;
  private LocalDateTime date;
  private String jobRunId;
  private Boolean logsStored;

  TrivyRunDto toDto() {
    return new TrivyRunDto(id, date, jobRunId, logsStored);
  }
}
