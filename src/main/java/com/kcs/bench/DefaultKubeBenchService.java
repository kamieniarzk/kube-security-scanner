package com.kcs.bench;

import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.kcs.NoDataFoundException;
import com.kcs.k8s.KubernetesApiClientWrapper;
import com.kcs.shared.ScanRepository;
import com.kcs.shared.ScanRunCreate;
import com.kcs.shared.ScanType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.kcs.util.KubeApiUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultKubeBenchService implements KubeBenchService {

  private final ScanRepository scanRepository;
  private final KubernetesApiClientWrapper k8sApi;

  @Override
  public String run() {
    deleteExistingJobIfNecessary();
    k8sApi.createJobFromYamlUrl("https://raw.githubusercontent.com/aquasecurity/kube-bench/main/job.yaml"); // TODO - extract to properties
    return persistRunData();
  }

  @Override
  public InputStream getPreviousRunLogs() {
    var lastPodName = scanRepository.getMostRecentScanByType(ScanType.KUBE_BENCH).podName();
    if (lastPodName == null || lastPodName.isBlank()) {
      throw new NoDataFoundException();
    }

    return k8sApi.streamPodLogs(lastPodName);
  }

  private String persistRunData() {
    var kubeBenchPod = k8sApi.getPodsWithPrefixSortedByCreation("kube-bench").stream().findFirst();

    if (kubeBenchPod.isPresent()) {
      return this.scanRepository.save(new ScanRunCreate(ScanType.KUBE_BENCH, kubeBenchPod.get().getMetadata().getName()));
    }

    log.warn("Failed to find pod related to kube bench run");
    return this.scanRepository.save(new ScanRunCreate(ScanType.KUBE_BENCH, null));
  }

  private void deleteExistingJobIfNecessary() {
    k8sApi.findJobWithPrefix("kube-bench")
        .ifPresent(v1Job -> performApiCall(() -> k8sApi.deleteJob(v1Job)));
  }
}
