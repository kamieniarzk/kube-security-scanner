package com.kcs.workload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class K8sResource {

  private final String kind;
  private final String namespace;
  private final String name;
  @Setter
  private List<Check> checks;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    K8sResource that = (K8sResource) o;
    return Objects.equals(kind, that.kind) && Objects.equals(namespace, that.namespace) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kind, namespace, name);
  }
}
