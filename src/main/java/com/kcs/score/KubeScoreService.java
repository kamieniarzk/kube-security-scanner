package com.kcs.score;

import com.kcs.score.persistence.document.KubeScoreRun;

import java.util.List;

public interface KubeScoreService {
  String score(String namespace);
  List<KubeScoreRun> getByNamespace(String namespace);
}
