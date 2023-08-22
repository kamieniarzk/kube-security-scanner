package com.kcs.shared;

import java.io.InputStream;

public interface LogRepository {
  void save(InputStream content, String directory, String fileName);
  void save(String content, String directory, String fileName);
  String getAsString(String directory, String fileName);
}
