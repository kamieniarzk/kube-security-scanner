package com.kcs.scheduling;

import org.springframework.data.mongodb.repository.MongoRepository;

interface ScheduledRunRepository extends MongoRepository<ScheduledRun, String> {
}
