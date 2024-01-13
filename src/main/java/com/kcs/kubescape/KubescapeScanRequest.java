package com.kcs.kubescape;

import java.util.Set;

public record KubescapeScanRequest(Set<KubescapeFramework> frameworks, String additionalFlags, Set<String> namespaces) {
}
