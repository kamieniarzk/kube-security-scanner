package com.kcs.kubebench;

import com.kcs.context.ClusterType;

import java.util.Set;

final class JobDefinitionLocator {
  private static final String GENERIC = "https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml";
  private static final String GENERIC_MASTER = "https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job-master.yaml";
  private static final String GKE = "https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job-gke.yaml";
  private static final String EKS = "https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job-eks.yaml";

  private JobDefinitionLocator() {}

  static String getJobDefinitionUrl(ClusterType clusterType, boolean controlPlane) {
    if (controlPlane && Set.of(ClusterType.GKE, ClusterType.EKS).contains(clusterType)) {
      throw new IllegalArgumentException();
    }

    return switch (clusterType) {
      case GKE -> GKE;
      case EKS -> EKS;
      default -> {
        if (controlPlane) {
          yield GENERIC_MASTER;
        }
        yield GENERIC;
      }
    };
  }
}
