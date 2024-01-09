package com.kcs.score;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface KubeScoreRunRepository extends MongoRepository<KubeScoreRunDto, String> {
  List<KubeScoreRunDto> findByNamespace(String namespace);
}
