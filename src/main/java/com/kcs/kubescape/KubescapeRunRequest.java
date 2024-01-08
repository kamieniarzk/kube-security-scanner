package com.kcs.kubescape;

import java.util.Set;

public record KubescapeRunRequest(Set<KubescapeFramework> frameworks, String additionalFlags) {
}
