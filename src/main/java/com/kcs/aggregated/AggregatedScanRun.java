package com.kcs.aggregated;

import com.kcs.shared.ScanRunStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("aggregatedScanRun")
@Builder(access = AccessLevel.PACKAGE)
public class AggregatedScanRun {
  private String id;
  private LocalDateTime date;
  private String scoreRunId;
  private String trivyRunId;
  private String kubescapeRunId;
  private ScanRunStatus status;
}
