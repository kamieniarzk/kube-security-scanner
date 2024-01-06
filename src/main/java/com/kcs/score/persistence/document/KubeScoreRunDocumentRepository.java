package com.kcs.score.persistence.document;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

interface KubeScoreRunDocumentRepository extends MongoRepository<KubeScoreRunDocument, String> {
  List<KubeScoreRunDocument> findByNamespace(String namespace);
}
