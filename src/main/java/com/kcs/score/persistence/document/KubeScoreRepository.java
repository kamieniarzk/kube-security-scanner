package com.kcs.score.persistence.document;

import java.util.List;

public interface KubeScoreRepository {
  String save(KubeScoreRunCreate runCreate);
  List<KubeScoreRunDto> getByNamespace(String namespace);
  KubeScoreRunDto getById(String id);
}
