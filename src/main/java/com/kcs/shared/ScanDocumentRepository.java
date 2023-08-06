package com.kcs.shared;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

interface ScanDocumentRepository extends MongoRepository<ScanRunDocument, String> {
  @Query(value = "{ 'type' : ?0 }", sort = "{ 'date' : -1 }")
  Optional<ScanRunDocument> findMostRecentByType(ScanType type);
}
