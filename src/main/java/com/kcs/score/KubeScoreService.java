package com.kcs.score;

import com.kcs.score.persistence.document.KubeScoreRunDto;

import java.util.List;

public interface KubeScoreService {
  String score(String namespace);
  List<KubeScoreRunDto> getByNamespace(String namespace);
}
