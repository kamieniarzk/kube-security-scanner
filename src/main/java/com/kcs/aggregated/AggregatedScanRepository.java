package com.kcs.aggregated;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AggregatedScanRepository extends MongoRepository<AggregatedScanRun, String> {
}
