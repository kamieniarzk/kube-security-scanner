package com.kcs.bench.persistence;

import com.kcs.context.ClusterType;

import java.time.LocalDateTime;

public record KubeBenchRunDto(
    String id,
    LocalDateTime date,
    String jobRunId,
    Boolean logsStored,
    String clusterName,
    ClusterType clusterType) {
}
