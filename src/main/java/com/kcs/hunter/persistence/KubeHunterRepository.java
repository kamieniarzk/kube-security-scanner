package com.kcs.hunter.persistence;

import java.util.List;

public interface KubeHunterRepository {
  KubeHunterRunDto save(KubeHunterRunCreate runCreate);
  KubeHunterRunDto get(String id);
  List<KubeHunterRunDto> getAll();
  KubeHunterRunDto getMostRecentRun();
  List<KubeHunterRunDto> getAllWithoutStoredLogs();
  void updateLogsStored(String id, Boolean logsStored);
}
