package com.kcs.bench;

import com.kcs.bench.dto.KubeBenchJsonResultDto;

public interface KubeBenchResultService {
  KubeBenchJsonResultDto getResult(String id);
}
