package com.kcs.bench.persistence;

import com.kcs.context.ClusterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("kubeBench")
@AllArgsConstructor
class KubeBenchRunDocument {
  private @Id String id;
  private LocalDateTime date;
  private String jobRunId;
  private Boolean logsStored;
  private String clusterName;
  private ClusterType clusterType;

  KubeBenchRunDto toDto() {
    return new KubeBenchRunDto(this.id, date, jobRunId, logsStored, clusterName, clusterType);
  }
}
