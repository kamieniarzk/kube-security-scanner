package com.kcs.bench.persistence;

import java.util.List;

public interface KubeBenchRepository {
  KubeBenchRunDto save(KubeBenchRunCreate jobRunCreate);
  KubeBenchRunDto get(String id);
  List<KubeBenchRunDto> getAll();
  KubeBenchRunDto getMostRecentRun();
  List<KubeBenchRunDto> getAllWithoutStoredLogs();
  void updateLogsStored(String id, Boolean logsStored);
}
