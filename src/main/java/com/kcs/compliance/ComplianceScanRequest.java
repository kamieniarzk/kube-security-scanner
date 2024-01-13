package com.kcs.compliance;

import com.kcs.kubescape.KubescapeFramework;

import java.util.Set;

public record ComplianceScanRequest(KubescapeFramework framework, Set<String> namespaces) {
}
