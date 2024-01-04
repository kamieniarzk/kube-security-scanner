package com.kcs.aggregated.persistence;

import com.kcs.aggregated.AggregatedScanRunDto;

import java.util.List;

public interface AggregatedRunRepository {
  AggregatedScanRunDto save(String scoreId, String trivyId);
  AggregatedScanRunDto get(String id);
  List<AggregatedScanRunDto> getAll();
}
