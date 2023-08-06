package com.kcs.shared;

import java.util.List;

public interface ScanRepository {
  String save(ScanRunCreate scanRunCreate);
  ScanRun get(String id);
  List<ScanRun> getAll();
  ScanRun getMostRecentScanByType(ScanType type);
  List<ScanRun> getAllWithoutStoredLogs();
  void updateLogsStored(String id, Boolean logsStored);
}
