package com.kcs.aggregated;

import com.kcs.shared.ScanResult;

public interface ResultAggregator {
  ScanResult aggregate(ScanResult... results);
}
