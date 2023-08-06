package com.kcs.shared;

import java.util.List;

public interface ScanRepository {
  String save(ScanRun scanRun);
  ScanRun get(String id);
  List<ScanRun> getAll();
  ScanRun getMostRecentScanByType(ScanType type);
}
