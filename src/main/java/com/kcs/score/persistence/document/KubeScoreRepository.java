package com.kcs.score.persistence.document;

import java.util.List;

public interface KubeScoreRepository {
  String save(KubeScoreRunCreate runCreate);
  List<KubeScoreRun> getByNamespace(String namespace);
}
