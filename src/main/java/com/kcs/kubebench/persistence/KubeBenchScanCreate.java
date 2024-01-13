package com.kcs.kubebench.persistence;

import com.kcs.context.ClusterType;

public record KubeBenchScanCreate(String jobRunId, String clusterName, ClusterType clusterType) {}
