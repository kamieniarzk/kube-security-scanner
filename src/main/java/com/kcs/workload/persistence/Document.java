package com.kcs.workload.persistence;

import com.kcs.workload.AggregatedScanRunDto;
import com.kcs.workload.ScanRunStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@org.springframework.data.mongodb.core.mapping.Document("aggregatedScanRun")
@AllArgsConstructor
class Document {

  @Id
  private String id;
  private LocalDateTime date;
  private String scoreId;
  private String trivyId;
  private ScanRunStatus status;

  AggregatedScanRunDto toDto() {
    return new AggregatedScanRunDto(id, date, scoreId, trivyId, status);
  }
}
