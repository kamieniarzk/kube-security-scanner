package com.kcs.bench;

import com.kcs.bench.persistence.KubeBenchRunDto;

import java.util.List;

public interface KubeBenchService {
  /**
   *
   * @return DTO of persisted run metadata
   */
  KubeBenchRunDto run(Boolean master);
  List<KubeBenchRunDto> getAll();
}
