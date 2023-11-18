package com.kcs.shared;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@RequiredArgsConstructor
public abstract class AbstractLogRepository {

  private final LogRepository logRepository;

  public String save(InputStream content, String fileName) {
    return logRepository.save(content, getDirectory(), fileName);
  }
  public String save(String content, String fileName) {
    return logRepository.save(content, getDirectory(), fileName);
  }
  public String getAsString(String fileName) {
    return logRepository.getAsString(getDirectory(), fileName);
  }

  public void delete(String fileName) {
    logRepository.delete(getDirectory(), fileName);
  }

  protected abstract String getDirectory();
}
