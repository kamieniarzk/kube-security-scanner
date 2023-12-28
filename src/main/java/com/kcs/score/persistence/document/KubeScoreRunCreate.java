package com.kcs.score.persistence.document;

public record KubeScoreRunCreate(Boolean namespaced, String namespace) {

  public KubeScoreRunCreate(String namespace) {
    this(true, namespace);
  }

  public KubeScoreRunCreate(Boolean namespaced) {
    this(namespaced, null);

    if (namespaced == null || namespaced) {
      throw new IllegalArgumentException();
    }
  }
}
