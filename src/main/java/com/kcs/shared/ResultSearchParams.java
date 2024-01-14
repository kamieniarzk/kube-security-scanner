package com.kcs.shared;

import java.util.Set;

public record ResultSearchParams(Set<String> namespace,
                                 Set<String> kind,
                                 Set<String> name,
                                 Set<Severity> severity,
                                 String origin,
                                 String originId) {
  public ResultSearchParams() {
    this(null, null, null, null, null, null);
  }
}
