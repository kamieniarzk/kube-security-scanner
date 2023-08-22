package com.kcs.bench.persistence.dto;

import java.util.List;

public interface KubeBenchRepository {
  String save(KubeBenchRunCreate kubeBenchRunCreate);
  KubeBenchRun get(String id);
  List<KubeBenchRun> getAll();
  KubeBenchRun getMostRecentRun();
  List<KubeBenchRun> getAllWithoutStoredLogs();
  void updateLogsStored(String id, Boolean logsStored);
}
