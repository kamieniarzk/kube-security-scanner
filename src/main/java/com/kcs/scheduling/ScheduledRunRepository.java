package com.kcs.scheduling;

import com.kcs.aggregated.AggregatedScanRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

interface ScheduledRunRepository extends MongoRepository<ScheduledRun, String> {
  Optional<ScheduledRun> findByCronExpressionAndAggregatedScanRequest(String cronExpression, AggregatedScanRequest runRequest);
}
