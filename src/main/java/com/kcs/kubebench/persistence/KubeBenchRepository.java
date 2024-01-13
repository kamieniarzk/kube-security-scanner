package com.kcs.kubebench.persistence;

import java.util.List;

public interface KubeBenchRepository {
  KubeBenchScanDto save(KubeBenchScanCreate jobRunCreate);
  KubeBenchScanDto get(String id);
  List<KubeBenchScanDto> getAll();
  KubeBenchScanDto getMostRecentRun();
  List<KubeBenchScanDto> getAllWithoutStoredLogs();
  void updateLogsStored(String id, Boolean logsStored);
}
