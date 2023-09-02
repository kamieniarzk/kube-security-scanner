package com.kcs.hunter.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("kubeHunter")
@AllArgsConstructor
class KubeHunterRunDocument {
  private @Id String id;
  private LocalDateTime date;
  private String args;
  private String jobRunId;
  private Boolean logsStored;

  KubeHunterRunDto toDto() {
    return new KubeHunterRunDto(id, date, args, jobRunId, logsStored);
  }
}
