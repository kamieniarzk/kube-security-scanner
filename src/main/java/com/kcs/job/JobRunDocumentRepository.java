package com.kcs.job;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

interface JobRunDocumentRepository extends MongoRepository<JobRunDocument, String> {
  @Query(sort = "{ 'date' : -1 }")
  List<JobRunDocument> findSortedByDate();

  @Query("{ 'logsStored' : { $in: [false, null] } }")
  List<JobRunDocument> findWhereLogsStoredNullOrFalse();
}

