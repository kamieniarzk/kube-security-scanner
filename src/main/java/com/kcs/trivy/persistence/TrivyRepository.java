package com.kcs.trivy.persistence;

import java.util.List;

public interface TrivyRepository {
  TrivyRunDto save(TrivyRunCreate runCreate);
  TrivyRunDto get(String id);
  List<TrivyRunDto> getAll();
  TrivyRunDto getMostRecentRun();
  List<TrivyRunDto> getAllWithoutStoredLogs();
  void updateLogsStored(String id, Boolean logsStored);
}
