package com.kcs.hunter.result;

import com.kcs.hunter.persistence.KubeHunterRunDto;

public interface KubeHunterResultService {
  void persistRunLogsForRunsWithoutStoredLogs();
  void persistRunLogs(KubeHunterRunDto kubeHunterRunDto);
}
