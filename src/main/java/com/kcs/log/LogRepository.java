package com.kcs.log;

import java.io.InputStream;

public interface LogRepository {
  void save(InputStream log, String podName);
  String getAsString(String podName);
}
