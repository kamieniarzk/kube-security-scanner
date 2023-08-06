package com.kcs.log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
class FilesystemLogRepository implements LogRepository {

  private static final String LOG_FILE_EXTENSION = ".log";

  private final String logsDirectory;

  public FilesystemLogRepository(@Value("${filesystem.log-location:}") String logsDirectory) {
    this.logsDirectory = logsDirectory;
  }

  @Override
  public void save(InputStream logStream, String podName) {
    try {
      Files.copy(logStream, Path.of(constructLogFilePath(podName)), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ioException) {
      log.error("Failed to save log content into: {}", constructLogFilePath(podName));
      throw new RuntimeException(ioException);
    }
  }

  @Override
  public String getAsString(final String podName) {
    try {
      return Files.readString(Path.of(constructLogFilePath(podName)));
    } catch (IOException ioException) {
      log.error("Failed to read log from file: {}", constructLogFilePath(podName));
      throw new RuntimeException(ioException);
    }
  }

  private String constructLogFilePath(String podName) {
    return Paths.get(logsDirectory, podName + LOG_FILE_EXTENSION).toString();
  }
}
