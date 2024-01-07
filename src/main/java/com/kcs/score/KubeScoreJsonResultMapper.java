package com.kcs.score;

import com.kcs.workload.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
class KubeScoreJsonResultMapper implements ResultMapper<List<KubeScoreJsonResultDto>> {

  private static final String ORIGIN_FORMAT = "kube-score ID: %s";

  @Override
  public WorkloadScanResult map(List<KubeScoreJsonResultDto> kubeScoreJsonResultDto) {
    var resources = kubeScoreJsonResultDto.stream()
        .map(KubeScoreJsonResultMapper::map)
        .toList();

    var resourcesMap = resources.stream()
        .collect(Collectors.groupingBy(K8sResource::getNamespace));

    return new WorkloadScanResult(resourcesMap);
  }

  private static K8sResource map(KubeScoreJsonResultDto scoreResult) {
    var namespace = scoreResult.getObjectMeta().getNamespace();
    var kind = ownerReferencesEmpty(scoreResult) ? scoreResult.getTypeMeta().getKind() : scoreResult.getObjectMeta().getOwnerReferences().get(0).getKind();
    var name = ownerReferencesEmpty(scoreResult) ? scoreResult.getObjectMeta().getName() : scoreResult.getObjectMeta().getOwnerReferences().get(0).getName();
    var vulnerabilities = mapChecksToVulnerabilities(scoreResult.getChecks());
    return new K8sResource(kind, namespace, name, vulnerabilities);
  }

  private static boolean ownerReferencesEmpty(KubeScoreJsonResultDto scoreResult) {
    return scoreResult.getObjectMeta().getOwnerReferences() == null || scoreResult.getObjectMeta().getOwnerReferences().isEmpty();
  }

  private static List<Vulnerability> mapChecksToVulnerabilities(List<KubeScoreJsonResultDto.Check> checks) {
    return checks.stream()
        .map(KubeScoreJsonResultMapper::mapCheckToVulnerabilities)
        .flatMap(Collection::stream)
        .toList();
  }

  @NotNull
  private static List<Vulnerability> mapCheckToVulnerabilities(KubeScoreJsonResultDto.Check check) {
    return check.getComments().stream()
        .map(comment -> mapCommentToVulnerability(check, comment))
        .toList();
  }

  @NotNull
  private static Vulnerability mapCommentToVulnerability(KubeScoreJsonResultDto.Check check, KubeScoreJsonResultDto.Comment comment) {
    var title = comment.getPath().concat(" ").concat(comment.getSummary());
    var description = check.getCheck().getComment();
    var remediation = comment.getDescription();
    return new Vulnerability(map(check.getGrade()), title, description, remediation, buildOrigin(check.getCheck()));
  }

  private static String buildOrigin(KubeScoreJsonResultDto.CheckData checkData) {
    return ORIGIN_FORMAT.formatted(checkData.getId());
  }

  private static Severity map(int grade) {
    return switch (grade) {
      case 1 -> Severity.MEDIUM;
      case 5 -> Severity.HIGH;
      default -> Severity.CRITICAL;
    };
  }
}
