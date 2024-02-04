package com.kcs.kubescore;

import com.kcs.shared.Check;
import com.kcs.shared.KubernetesResource;
import com.kcs.shared.ScanResult;
import com.kcs.shared.Severity;
import com.kcs.aggregated.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
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
    var kind = ownerReferencesEmpty(scoreResult) ? scoreResult.getTypeMeta().getKind() : scoreResult.getObjectMeta().getOwnerReferences().get(0).getKind();
    var name = ownerReferencesEmpty(scoreResult) ? scoreResult.getObjectMeta().getName() : scoreResult.getObjectMeta().getOwnerReferences().get(0).getName();
    var checks = mapChecks(scoreResult.getChecks());
    return new KubernetesResource(kind, namespace, name, checks);
  }

  private static boolean ownerReferencesEmpty(KubeScoreJsonResultDto scoreResult) {
    return scoreResult.getObjectMeta().getOwnerReferences() == null || scoreResult.getObjectMeta().getOwnerReferences().isEmpty();
  }

  private static List<Check> mapChecks(List<KubeScoreJsonResultDto.Check> checks) {
    return checks.stream()
        .map(KubeScoreJsonResultMapper::mapChecks)
        .flatMap(Collection::stream)
        .toList();
  }

  @NotNull
  private static List<Check> mapChecks(KubeScoreJsonResultDto.Check check) {
    return check.getComments().stream()
        .map(comment -> mapCommentToCheckInstance(check, comment))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  @NotNull
  private static Optional<Check> mapCommentToCheckInstance(KubeScoreJsonResultDto.Check check, KubeScoreJsonResultDto.Comment comment) {
    if (check.getGrade() > 1) {
      return Optional.empty();
    }
    var title = comment.getPath().concat(" ").concat(comment.getSummary());
    var description = check.getCheck().getComment();
    var remediation = comment.getDescription();
    return Optional.of(new Check(Severity.KUBE_SCORE, title, description, remediation, ORIGIN, check.getCheck().getId()));
  }
}
