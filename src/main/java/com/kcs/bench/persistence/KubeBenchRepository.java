package com.kcs.bench.persistence;

import java.util.List;

import com.kcs.bench.dto.KubeBenchRunDto;

public interface KubeBenchRepository {
  KubeBenchRunDto save(KubeBenchRunCreate jobRunCreate);
  KubeBenchRunDto get(String id);
  List<KubeBenchRunDto> getAll();
  KubeBenchRunDto getMostRecentRun();
  List<KubeBenchRunDto> getAllWithoutStoredLogs();
  void updateLogsStored(String id, Boolean logsStored);
}
