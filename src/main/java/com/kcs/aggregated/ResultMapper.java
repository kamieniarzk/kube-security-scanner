package com.kcs.aggregated;

import com.kcs.shared.ScanResult;

public interface ResultMapper<Source> {
  ScanResult map(Source source);
}
