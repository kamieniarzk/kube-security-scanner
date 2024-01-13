package com.kcs.trivy;

public interface TrivyRepository {
  TrivyScanDto save(TrivyScanCreate runCreate);
  TrivyScanDto get(String id);
}
