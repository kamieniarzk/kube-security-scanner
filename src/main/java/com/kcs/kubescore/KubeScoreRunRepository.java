package com.kcs.kubescore;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface KubeScoreRunRepository extends MongoRepository<KubeScoreScanDto, String> {
  List<KubeScoreScanDto> findByNamespace(String namespace);
}
