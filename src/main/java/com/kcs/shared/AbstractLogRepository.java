package com.kcs.shared;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@RequiredArgsConstructor
public abstract class AbstractLogRepository {

  private final LogRepository logRepository;

  public void save(InputStream content, String fileName) {
    logRepository.save(content, getDirectory(), fileName);
  }
  public void save(String content, String fileName) {
    logRepository.save(content, getDirectory(), fileName);
  }
  public String getAsString(String fileName) {
    return logRepository.getAsString(getDirectory(), fileName);
  }

  protected abstract String getDirectory();
}
