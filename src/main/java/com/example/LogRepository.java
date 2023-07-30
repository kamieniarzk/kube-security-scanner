package com.example;

public interface LogRepository {
  Long save(String log);
  String find(Long id);
}
