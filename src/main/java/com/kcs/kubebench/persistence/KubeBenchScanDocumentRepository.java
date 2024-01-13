package com.kcs.kubebench.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

interface KubeBenchScanDocumentRepository extends MongoRepository<KubeBenchScanDocument, String> {
  @Query(sort = "{ 'date' : -1 }")
  List<KubeBenchScanDocument> findSortedByDate();

  @Query("{ 'logsStored' : { $in: [false, null] } }")
  List<KubeBenchScanDocument> findWhereLogsStoredNullOrFalse();
}
