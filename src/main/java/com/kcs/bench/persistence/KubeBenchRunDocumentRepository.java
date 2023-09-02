package com.kcs.bench.persistence;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

interface KubeBenchRunDocumentRepository extends MongoRepository<KubeBenchRunDocument, String> {
  @Query(sort = "{ 'date' : -1 }")
  List<KubeBenchRunDocument> findSortedByDate();

  @Query("{ 'logsStored' : { $in: [false, null] }, 'podName' : {$exists: true} }")
  List<KubeBenchRunDocument> findWhereLogsStoredNullOrFalseAndPodNameExists();
}
