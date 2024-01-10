package com.kcs.workload;

import com.kcs.kubescape.KubescapeRunRequest;
import com.kcs.score.KubeScoreRunRequest;
import com.kcs.trivy.TrivyRunRequest;

public record AggregatedRunRequest(KubescapeRunRequest kubescapeRunRequest, TrivyRunRequest trivyRunRequest, KubeScoreRunRequest kubeScoreRunRequest) {
}
