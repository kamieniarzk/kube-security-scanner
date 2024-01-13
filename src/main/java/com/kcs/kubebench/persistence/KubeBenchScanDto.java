package com.kcs.kubebench.persistence;

import com.kcs.context.ClusterType;

import java.time.LocalDateTime;

public record KubeBenchScanDto(
    String id,
    LocalDateTime date,
    String jobRunId,
    Boolean logsStored,
    String clusterName,
    ClusterType clusterType) {
}
