package com.kcs.log;

public interface LogRepository {
  Long save(String log);
  String find(Long id);
}
