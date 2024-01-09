package com.kcs.trivy;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

interface TrivyRunRepository extends MongoRepository<TrivyRunDto, String> {
  @Query(sort = "{ 'date' : -1 }")
  List<TrivyRunDto> findSortedByDate();

  @Query("{ 'logsStored': { $in: [false, null] } }")
  List<TrivyRunDto> findWhereLogsStoredNullOrFalse();
}
