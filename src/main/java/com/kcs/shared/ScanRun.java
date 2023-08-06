package com.kcs.shared;

import java.time.LocalDateTime;

public record ScanRun(String id, LocalDateTime date, ScanType type, String podName) {

  public ScanRun(ScanType type, String podName) {
    this(null, LocalDateTime.now(), type, podName);
  }
}
