package com.kcs.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SchedulerFacade {

  private final Scheduler scheduler;
  private final ScheduledRunRepository repository;

  public String schedule(ScheduledRunRequest runRequest) {
    return scheduler.scheduleAndPersist(runRequest);
  }

  public boolean unschedule(String runId) {
    return scheduler.unschedule(runId);
  }

  public List<ScheduledRun> getAllScheduledRuns() {
    return repository.findAll();
  }
}
