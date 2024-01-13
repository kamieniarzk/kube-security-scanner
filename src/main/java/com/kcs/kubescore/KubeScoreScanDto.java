package com.kcs.kubescore;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("kubeScore")
public record KubeScoreScanDto(String id, LocalDateTime date, String namespace) {
  public KubeScoreScanDto(String namespace) {
    this(null, LocalDateTime.now(), namespace);
  }

  public KubeScoreScanDto() {
    this(null, LocalDateTime.now(), null);
  }
}
