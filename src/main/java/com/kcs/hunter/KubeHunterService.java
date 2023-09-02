package com.kcs.hunter;

import com.kcs.hunter.persistence.KubeHunterRunDto;

import java.util.List;

public interface KubeHunterService {
  /**
   *
   * @return DTO of persisted run metadata
   */
  KubeHunterRunDto run(String args);
  List<KubeHunterRunDto> getAll();
}
