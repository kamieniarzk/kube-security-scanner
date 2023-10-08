package com.kcs.bench.persistence;

import com.kcs.context.ClusterType;

public record KubeBenchRunCreate(String jobRunId, String clusterName, ClusterType clusterType) {}
