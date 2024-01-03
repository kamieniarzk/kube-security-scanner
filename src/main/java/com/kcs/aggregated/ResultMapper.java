package com.kcs.aggregated;

public interface ResultMapper<Source> {
  AggregatedScanResult map(Source source);
}
