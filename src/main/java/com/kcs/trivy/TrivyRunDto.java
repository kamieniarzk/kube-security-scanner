package com.kcs.trivy;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@Document("trivyRun")
@Builder(access = AccessLevel.PACKAGE)
public class TrivyRunDto {
  private String id;
  private LocalDateTime date;
  private String jobRunId;
  private Boolean logsStored;
  private Set<TrivyScanner> scanners;
  private TrivyCompliance compliance;
  private String command;
  private Set<TrivySeverity> severity;
}
