package com.kcs.bench;

import com.kcs.bench.dto.KubeBenchRunDto;
import com.kcs.bench.dto.KubeBenchTarget;

import java.util.List;

public interface KubeBenchService {
  /**
   *
   * @return DTO of persisted run metadata
   */
  KubeBenchRunDto run(KubeBenchTarget target);
  List<KubeBenchRunDto> getAll();
}
