package com.kcs.bench;

import com.kcs.context.ClusterType;

final class JobDefinitionLocator {
  private static final String GENERIC = "https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml";
  private static final String GKE = "https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job-gke.yaml";
  private static final String EKS = "https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job-eks.yaml";

  private JobDefinitionLocator() {}

  static String getJobDefinitionUrl(ClusterType clusterType) {
    return switch (clusterType) {
      case GKE -> GKE;
      case EKS -> EKS;
      default -> GENERIC;
    };
  }
}
