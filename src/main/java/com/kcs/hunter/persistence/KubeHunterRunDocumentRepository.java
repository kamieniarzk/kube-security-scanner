package com.kcs.hunter.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

interface KubeHunterRunDocumentRepository extends MongoRepository<KubeHunterRunDocument, String> {
  @Query(sort = "{ 'date' : -1 }")
  List<KubeHunterRunDocument> findSortedByDate();

  @Query("{ 'logsStored': { $in: [false, null] } }")
  List<KubeHunterRunDocument> findWhereLogsStoredNullOrFalse();
}
