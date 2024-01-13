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
    var vulnerabilities = mapChecksToVulnerabilities(scoreResult.getChecks());
    return new KubernetesResource(kind, namespace, name, vulnerabilities);
  }

  private static boolean ownerReferencesEmpty(KubeScoreJsonResultDto scoreResult) {
    return scoreResult.getObjectMeta().getOwnerReferences() == null || scoreResult.getObjectMeta().getOwnerReferences().isEmpty();
  }

  private static List<Check> mapChecksToVulnerabilities(List<KubeScoreJsonResultDto.Check> checks) {
    return checks.stream()
        .map(KubeScoreJsonResultMapper::mapCheckToVulnerabilities)
        .flatMap(Collection::stream)
        .toList();
  }

  @NotNull
  private static List<Check> mapCheckToVulnerabilities(KubeScoreJsonResultDto.Check check) {
    return check.getComments().stream()
        .map(comment -> mapCommentToVulnerability(check, comment))
        .toList();
  }

  @NotNull
  private static Check mapCommentToVulnerability(KubeScoreJsonResultDto.Check check, KubeScoreJsonResultDto.Comment comment) {
    var title = comment.getPath().concat(" ").concat(comment.getSummary());
    var description = check.getCheck().getComment();
    var remediation = comment.getDescription();
    return new Check(map(check.getGrade()), title, description, remediation, ORIGIN, check.getCheck().getId());
  }

  private static Severity map(int grade) {
    return switch (grade) {
      case 1 -> Severity.MEDIUM;
      case 5 -> Severity.HIGH;
      default -> Severity.CRITICAL;
    };
  }
}
