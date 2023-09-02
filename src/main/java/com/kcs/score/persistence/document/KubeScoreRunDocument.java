package com.kcs.score.persistence.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("kubeScore")
record KubeScoreRunDocument(@Id String id, LocalDateTime time, String namespace) {
  KubeScoreRunDto toDto() {
    return new KubeScoreRunDto(id, time, namespace);
  }
}
