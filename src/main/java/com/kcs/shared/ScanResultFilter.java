package com.kcs.shared;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScanResultFilter {

  private final ResultSearchParams searchParams;

  public static ScanResultFilter withParams(ResultSearchParams params) {
    return new ScanResultFilter(params);
  }

  public ScanResult filter(ScanResult result) {
    return new ScanResult(
        result.getScanId(),
        result.getAggregated(),
        filterNamespacedResources(result.getNamespacedResources()),
        filterResources(result.getNonNamespacedResources()),
        result.getSkippedChecks()
    );
  }

  @Nullable
  private Map<String, List<KubernetesResource>> filterNamespacedResources(Map<String, List<KubernetesResource>> namespacedResources) {
    if (namespacedResources == null) {
      return null;
    }

    var entrySet = namespacedResources.entrySet().stream();
    if (searchParams.namespace() != null && !searchParams.namespace().isEmpty()) {
      entrySet = entrySet.filter(entry -> searchParams.namespace().contains(entry.getKey()));
    }

    return entrySet.collect(Collectors.toMap(
        Map.Entry::getKey,
        entry -> filterResources(entry.getValue())
    ));
  }

  private List<KubernetesResource> filterResources(List<KubernetesResource> resources) {
    if (resources == null) {
      return Collections.emptyList();
    }
    return resources.stream()
        .filter(this::resourceFilter)
        .map(this::filterChecks)
        .filter(Objects::nonNull)
        .toList();
  }

  @Nullable
  private KubernetesResource filterChecks(KubernetesResource resource) {
    if (resource.getChecks() == null) {
      return resource;
    }
    var filteredChecks = resource.getChecks().stream()
        .filter(checkOriginAndSeverityPredicate())
        .toList();
    resource.setChecks(filteredChecks);
    return resource.getChecks().isEmpty() ? null : resource;
  }

  @NotNull
  private Predicate<Check> checkOriginAndSeverityPredicate() {
    return check ->
        (searchParams.origin() == null || searchParams.origin().equals(check.origin())) &&
        (searchParams.originId() == null || searchParams.originId().equals(check.originId())) &&
        (searchParams.severity() == null || searchParams.severity().isEmpty() || searchParams.severity().contains(check.severity()));
  }

  private boolean resourceFilter(KubernetesResource resource) {
    return
        (searchParams.name() == null || searchParams.name().isEmpty() || searchParams.name().contains(resource.getName())) &&
        (searchParams.kind() == null || searchParams.kind().isEmpty() || searchParams.kind().contains(resource.getKind()));
  }
}
