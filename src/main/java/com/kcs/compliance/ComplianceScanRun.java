package com.kcs.compliance;

import com.kcs.kubescape.KubescapeFramework;
import com.kcs.kubescape.KubescapeScan;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("complianceRun")
public record ComplianceScanRun(@Id String id, KubescapeFramework framework, LocalDateTime date, String kubescapeRunId) {
  public ComplianceScanRun(KubescapeScan kubescapeScan) {
    this(null,
        getFramework(kubescapeScan),
        LocalDateTime.now(),
        kubescapeScan.getId());
  }

  private static KubescapeFramework getFramework(KubescapeScan kubescapeScan) {
    if (kubescapeScan.getFrameworks() == null || kubescapeScan.getFrameworks().size() != 1) {
      throw getIllegalArgumentException();
    }
    return kubescapeScan.getFrameworks().stream().findFirst().orElseThrow(ComplianceScanRun::getIllegalArgumentException);
  }

  @NotNull
  private static IllegalArgumentException getIllegalArgumentException() {
    return new IllegalArgumentException("Can only construct compliance scan from a kubescape run one framework set");
  }
}
