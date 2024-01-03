package com.kcs.aggregated.persistence;

import com.kcs.aggregated.AggregatedScanRunDto;

public interface AggregatedRunRepository {
  AggregatedScanRunDto save(String scoreId, String trivyId);
  AggregatedScanRunDto get(String id);
}
