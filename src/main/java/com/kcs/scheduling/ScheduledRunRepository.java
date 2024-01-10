package com.kcs.scheduling;

import com.kcs.workload.AggregatedRunRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

interface ScheduledRunRepository extends MongoRepository<ScheduledRun, String> {
  Optional<ScheduledRun> findByCronExpressionAndAggregatedRunRequest(String cronExpression, AggregatedRunRequest runRequest);
}
