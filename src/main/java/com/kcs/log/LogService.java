package com.kcs.log;

import com.kcs.shared.ScanRun;

public interface LogService {
  void persistRunLogsForRunsWithoutStoredLogs();
  void persistRunLogs(ScanRun scanRun);
}
