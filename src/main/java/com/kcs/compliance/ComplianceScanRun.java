package com.kcs.compliance;

import com.kcs.kubescape.KubescapeFramework;
import com.kcs.kubescape.KubescapeRun;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("complianceRun")
public record ComplianceScanRun(@Id String id, KubescapeFramework framework, LocalDateTime date, String kubescapeRunId) {
  public ComplianceScanRun(KubescapeRun kubescapeRun) {
    this(null,
        getFramework(kubescapeRun),
        LocalDateTime.now(),
        kubescapeRun.getId());
  }

  private static KubescapeFramework getFramework(KubescapeRun kubescapeRun) {
    if (kubescapeRun.getFrameworks() == null || kubescapeRun.getFrameworks().size() != 1) {
      throw getIllegalArgumentException();
    }
    return kubescapeRun.getFrameworks().stream().findFirst().orElseThrow(ComplianceScanRun::getIllegalArgumentException);
  }

  @NotNull
  private static IllegalArgumentException getIllegalArgumentException() {
    return new IllegalArgumentException("Can only construct compliance scan from a kubescape run one framework set");
  }
}
