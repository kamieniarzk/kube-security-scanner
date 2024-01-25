package com.kcs.kubebench;

import com.kcs.context.ClusterType;

public record KubeBenchRunRequest(ClusterType clusterType, boolean controlPlane, boolean generic, String benchmark) {
}
