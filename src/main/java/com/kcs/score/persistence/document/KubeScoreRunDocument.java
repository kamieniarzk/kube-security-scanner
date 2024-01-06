package com.kcs.score.persistence.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("kubeScore")
record KubeScoreRunDocument(@Id String id, LocalDateTime time, Boolean namespaced, String namespace) {
  KubeScoreRunDto toDto() {
    return new KubeScoreRunDto(id, time, namespaced, namespace);
  }
}
