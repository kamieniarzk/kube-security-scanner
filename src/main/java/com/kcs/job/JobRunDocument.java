package com.kcs.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document("jobRun")
@AllArgsConstructor
class JobRunDocument {
  private @Id String id;
  private LocalDateTime date;
  private String podName;

  JobRunDto toDto() {
    return new JobRunDto(id, date, podName);
  }
}
