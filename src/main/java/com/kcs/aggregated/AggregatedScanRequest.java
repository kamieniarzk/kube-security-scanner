package com.kcs.aggregated;

import com.kcs.kubescape.KubescapeScanRequest;
import com.kcs.kubescore.KubeScoreScanRequest;
import com.kcs.trivy.TrivyRunRequest;

public record AggregatedScanRequest(KubescapeScanRequest kubescapeScanRequest, TrivyRunRequest trivyRunRequest, KubeScoreScanRequest kubeScoreScanRequest) {
}
