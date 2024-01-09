package com.kcs.trivy;

import java.util.List;

public interface TrivyRepository {
  TrivyRunDto save(TrivyRunCreate runCreate);
  TrivyRunDto get(String id);
}
