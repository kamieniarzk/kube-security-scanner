package com.kcs.kubescore;

import com.kcs.shared.Check;
import com.kcs.shared.KubernetesResource;
import com.kcs.shared.ScanResult;
import com.kcs.shared.Severity;
import com.kcs.aggregated.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
class KubeScoreJsonResultMapper implements ResultMapper<List<KubeScoreJsonResultDto>> {

  private static final String ORIGIN = "kube-score";

  @Override
  public ScanResult map(List<KubeScoreJsonResultDto> kubeScoreJsonResultDto) {
    var resources = kubeScoreJsonResultDto.stream()
        .map(KubeScoreJsonResultMapper::map)
        .toList();

    var resourcesMap = resources.stream()
        .collect(Collectors.groupingBy(KubernetesResource::getNamespace));

    return new ScanResult(resourcesMap);
  }

  private static KubernetesResource map(KubeScoreJsonResultDto scoreResult) {
    var namespace = scoreResult.getObjectMeta().getNamespace();
    var kind = scoreResult.getTypeMeta().getKind();
    var name = scoreResult.getObjectMeta().getName();
    var checks = mapChecks(scoreResult.getChecks());
    return new KubernetesResource(kind, namespace, name, checks);
  }

  private static List<Check> mapChecks(List<KubeScoreJsonResultDto.Check> checks) {
    return checks.stream()
        .map(KubeScoreJsonResultMapper::mapChecks)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  @NotNull
  private static Optional<Check> mapChecks(KubeScoreJsonResultDto.Check check) {
    if (check.getGrade() > 1) {
      return Optional.empty();
    }
    return Optional.of(new Check(Severity.UNKNOWN, check.getCheck().getName(), check.getCheck().getComment(), null, ORIGIN, check.getCheck().getId()));
  }
}
