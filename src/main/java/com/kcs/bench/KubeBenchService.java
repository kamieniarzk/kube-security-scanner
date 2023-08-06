package com.kcs.bench;

import java.io.InputStream;

public interface KubeBenchService {
  String run();
  InputStream getPreviousRunLogs();
}
