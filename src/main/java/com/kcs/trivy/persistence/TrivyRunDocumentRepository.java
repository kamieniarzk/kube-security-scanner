package com.kcs.trivy.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

interface TrivyRunDocumentRepository extends MongoRepository<TrivyRunDocument, String> {
  @Query(sort = "{ 'date' : -1 }")
  List<TrivyRunDocument> findSortedByDate();

  @Query("{ 'logsStored': { $in: [false, null] } }")
  List<TrivyRunDocument> findWhereLogsStoredNullOrFalse();
}
