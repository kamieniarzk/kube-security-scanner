package com.kcs.hunter;

import com.kcs.hunter.persistence.KubeHunterRepository;
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
  private final KubeHunterRepository hunterRepository;

  @Override
  public KubeHunterResultDto getResult(String id) {
    var runDto = hunterRepository.get(id);
    var jobDto = jobRunRepository.get(runDto.jobRunId());
    var podName = jobDto.podName();
    return KubeHunterResultParser.parse(logService.getLogs(podName));
  }
}
