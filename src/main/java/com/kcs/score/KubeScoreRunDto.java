package com.kcs.score;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("kubeScore")
public record KubeScoreRunDto(String id, LocalDateTime date, String namespace) {
  public KubeScoreRunDto(String namespace) {
    this(null, LocalDateTime.now(), namespace);
  }

  public KubeScoreRunDto() {
    this(null, LocalDateTime.now(), null);
  }
}
