package com.kcs.hunter;

import com.kcs.hunter.result.KubeHunterResultDto;

public interface KubeHunterResultService {
  KubeHunterResultDto getResult(String id);
}
