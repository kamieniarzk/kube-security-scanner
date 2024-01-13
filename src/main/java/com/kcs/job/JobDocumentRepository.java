package com.kcs.job;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

interface JobDocumentRepository extends MongoRepository<JobDocument, String> {
  @Query(sort = "{ 'date' : -1 }")
  List<JobDocument> findSortedByDate();

  @Query("{ 'logsStored' : { $in: [false, null] } }")
  List<JobDocument> findWhereLogsStoredNullOrFalse();
}

