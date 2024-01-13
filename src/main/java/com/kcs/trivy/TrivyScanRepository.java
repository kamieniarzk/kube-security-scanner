package com.kcs.trivy;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

interface TrivyScanRepository extends MongoRepository<TrivyScanDto, String> {
  @Query(sort = "{ 'date' : -1 }")
  List<TrivyScanDto> findSortedByDate();

  @Query("{ 'logsStored': { $in: [false, null] } }")
  List<TrivyScanDto> findWhereLogsStoredNullOrFalse();
}
