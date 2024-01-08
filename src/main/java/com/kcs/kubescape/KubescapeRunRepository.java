package com.kcs.kubescape;

import org.springframework.data.mongodb.repository.MongoRepository;

interface KubescapeRunRepository extends MongoRepository<KubescapeRun, String> {
}
