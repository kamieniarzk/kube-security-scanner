package com.kcs.compliance;

import org.springframework.data.mongodb.repository.MongoRepository;

interface ComplianceRunRepository extends MongoRepository<ComplianceScanRun, String> {
}
