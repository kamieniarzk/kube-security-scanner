package com.kcs.bench.persistence.log;

import com.kcs.bench.persistence.dto.KubeBenchRun;

public interface LogService {
  void persistRunLogsForRunsWithoutStoredLogs();
  void persistRunLogs(KubeBenchRun kubeBenchRun);
}
