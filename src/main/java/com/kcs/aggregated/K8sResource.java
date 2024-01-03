package com.kcs.aggregated;

import java.util.List;

public record K8sResource(String kind, String namespace, String name, List<Vulnerability> vulnerabilities) {
}
