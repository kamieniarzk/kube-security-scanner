package com.kcs.score.persistence.document;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

interface KubeScoreRunDocumentRepository extends MongoRepository<KubeScoreRunDocument, String> {
  List<KubeScoreRunDocument> findByNamespace(String namespace);
}
