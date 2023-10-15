package com.kcs.hunter;

import com.kcs.bench.persistence.KubeBenchRepository;
import com.kcs.hunter.result.KubeHunterResultDto;
import com.kcs.hunter.result.KubeHunterResultParser;
import com.kcs.job.JobRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DefaultKubeHunterResultService implements KubeHunterResultService {


  private final KubeHunterLogService logService;
  private final JobRunRepository jobRunRepository;
  private final KubeBenchRepository benchRepository;

  @Override
  public KubeHunterResultDto getResult(String id) {
    var runDto = benchRepository.get(id);
    var jobDto = jobRunRepository.get(runDto.jobRunId());
    var podName = jobDto.podName();
    return KubeHunterResultParser.parse(logService.getLogs(podName));
  }
}
