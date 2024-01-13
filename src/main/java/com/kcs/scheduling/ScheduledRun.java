package com.kcs.scheduling;

import com.kcs.aggregated.AggregatedScanRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("scheduledRun")
@Builder(access = AccessLevel.PACKAGE)
public class ScheduledRun {
  @Id
  private String id;
  private String cronExpression;
  private AggregatedScanRequest aggregatedScanRequest;
}
