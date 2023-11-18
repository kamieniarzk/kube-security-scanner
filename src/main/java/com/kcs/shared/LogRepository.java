package com.kcs.shared;

import java.io.InputStream;

public interface LogRepository {
  /**
   *
   * @param content
   * @param directory
   * @param fileName
   * @return path of saved entry
   */
  String save(InputStream content, String directory, String fileName);

  /**
   *
   * @param content
   * @param directory
   * @param fileName
   * @return path of saved entry
   */
  String save(String content, String directory, String fileName);
  String getAsString(String directory, String fileName);

  void delete(String directory, String fileName);
}
