package com.kcs.trivy;

import com.kcs.trivy.persistence.TrivyRunDto;

import java.util.List;

public interface TrivyService {
  /**
   *
   * @return DTO of persisted run metadata
   */
  TrivyRunDto run();
  List<TrivyRunDto> getAll();
}
