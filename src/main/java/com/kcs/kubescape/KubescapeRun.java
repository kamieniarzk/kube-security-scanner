package com.kcs.kubescape;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Document("kubescape")
class KubescapeRun {
  @Id
  private String id;
  private LocalDateTime date;
  private String command;
  private Set<KubescapeFramework> frameworks;

  public KubescapeRun(Set<KubescapeFramework> frameworks) {
    this.frameworks = frameworks;
    this.date = LocalDateTime.now();
  }
}
