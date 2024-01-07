package com.kcs.scheduling;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.scheduling.support.CronTrigger;

import java.time.Clock;

@Data
@Document("scheduledRun")
public class ScheduledRun {
  @Id
  private String Id;
  private String cronExpression;
  private RunType runType;

  @Transient
  @JsonIgnore
  CronTrigger getCronTrigger() {
    return new CronTrigger(cronExpression, Clock.systemDefaultZone().getZone());
  }
}
