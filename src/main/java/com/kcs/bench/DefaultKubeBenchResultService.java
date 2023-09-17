package com.kcs.bench;

import com.kcs.bench.dto.KubeBenchJsonResultDto;
import com.kcs.bench.persistence.KubeBenchRepository;
import com.kcs.job.JobRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DefaultKubeBenchResultService implements KubeBenchResultService {

  private final KubeBenchLogService logService;
  private final JobRunRepository jobRunRepository;
  private final KubeBenchRepository benchRepository;

  @Override
  public KubeBenchJsonResultDto getResult(String id) {
    var runDto = benchRepository.get(id);
    var jobDto = jobRunRepository.get(runDto.jobRunId());
    var podName = jobDto.podName();
    return KubeBenchJsonResultParser.parse(logService.getLogs(podName));
  }
}
