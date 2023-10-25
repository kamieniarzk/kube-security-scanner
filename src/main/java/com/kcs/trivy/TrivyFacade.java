package com.kcs.trivy;

import com.kcs.context.ContextHolder;
import com.kcs.k8s.KubernetesApiClientWrapper;
import com.kcs.util.MiscUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TrivyFacade {

  private final KubernetesApiClientWrapper k8sApi;
  private final ContextHolder contextHolder;

  public void runTrivyJob() {
    try {
      k8sApi.createJobFromYamlWithServiceAccount(TrivyJobDefinition.get(), MiscUtils.constructServiceAccountName(contextHolder.getHelmReleaseName()));
    } catch (IOException ioException) {
      throw new RuntimeException();
    }
  }
}
