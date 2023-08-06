package com.kcs.log;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
class InMemoryLogRepository implements LogRepository {

  private final Map<Long, String> logRepository = new HashMap<>();

  @Override
  public Long save(String log) {
    var ids = logRepository.keySet().stream().sorted().toList();
    var sequentialId = ids.isEmpty() ? 0 : ids.get(ids.size() - 1);
    logRepository.put(sequentialId, log);
    return sequentialId;
  }

  @Override
  public String find(Long id) {
    if (logRepository.containsKey(id)) {
      return logRepository.get(id);
    }
    throw new RuntimeException("No data found");
  }
}
