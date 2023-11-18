package com.kcs.shared;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Repository
@RequiredArgsConstructor
class FilesystemLogRepository implements LogRepository {

  @Override
  public String save(InputStream content, String directory, String fileName) {
    try {
      var path = Path.of(constructPathString(directory, fileName));
      path.getParent().toFile().mkdir();
      Files.copy(content, Path.of(constructPathString(directory, fileName)), StandardCopyOption.REPLACE_EXISTING);
      return path.toString();
    } catch (IOException ioException) {
      log.error("Failed to save content into: {}", constructPathString(directory, fileName));
      throw new RuntimeException(ioException);
    }
  }

  @Override
  public String save(String content, String directory, String fileName) {
    try {
      var path = Path.of(constructPathString(directory, fileName));
      path.getParent().toFile().mkdir();
      Files.write(path, content.getBytes());
      return path.toString();
    } catch (IOException ioException) {
      log.error("Failed to save content into: {}", constructPathString(directory, fileName));
      throw new RuntimeException(ioException);
    }
  }

  @Override
  public String getAsString(String directory, String fileName) {
    try {
      return Files.readString(Path.of(constructPathString(directory, fileName)));
    } catch (IOException ioException) {
      log.error("Failed to read content from file: {}", constructPathString(directory, fileName));
      throw new RuntimeException(ioException);
    }
  }

  @Override
  public void delete(String directory, String fileName) {
    try {
      Files.deleteIfExists(Path.of(directory, fileName));
    } catch (IOException ioException) {
      log.error("Failed to delete file {} from directory {}", fileName, directory);
      throw new RuntimeException(ioException);
    }
  }

  private String constructPathString(String directory, String fileName) {
    return Paths.get(directory, fileName).toString();
  }
}
