package com.kcs.scheduling;

import com.kcs.NoDataFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
class Scheduler {

  private final TaskScheduler taskScheduler;
  private final RunFactory runFactory;
  private final ScheduledRunRepository scheduledRunRepository;
  private final Map<String, ScheduledFuture<?>> inMemoryRuns = new ConcurrentHashMap<>();

  public Scheduler(TaskScheduler taskScheduler, RunFactory runFactory, ScheduledRunRepository scheduledRunRepository) {
    this.taskScheduler = taskScheduler;
    this.runFactory = runFactory;
    this.scheduledRunRepository = scheduledRunRepository;
    this.syncDatabaseAndMemory();
  }

  String scheduleAndPersist(ScheduledRunRequest runRequest) {
    var runnable = runFactory.create(runRequest.aggregatedRunRequest());
    var scheduledTask = taskScheduler.schedule(runnable, buildCronTrigger(runRequest.cronExpression()));
    var persistedScheduledRun = persistIfNeeded(runRequest);
    inMemoryRuns.put(persistedScheduledRun.getId(), scheduledTask);
    log.info("Scheduled run with cron: {} and id: {}", runRequest.cronExpression(), persistedScheduledRun.getId());
    return persistedScheduledRun.getId();
  }

  boolean unschedule(String runId) {
    var scheduledRun = scheduledRunRepository.findById(runId).orElseThrow(NoDataFoundException::new);
    scheduledRunRepository.delete(scheduledRun);
    final var removed = new AtomicBoolean(false);
    Optional.ofNullable(inMemoryRuns.remove(runId))
        .ifPresentOrElse(scheduledFuture -> {
          log.info("Unscheduling run with id {}", runId);
          scheduledFuture.cancel(true);
          removed.set(true);
        }, () -> log.info("There is no scheduled run with id: {}", runId));
    return removed.get();
  }

  private void schedule(ScheduledRun scheduledRun) {
    var runnable = runFactory.create(scheduledRun.getAggregatedRunRequest());
    var scheduledTask = taskScheduler.schedule(runnable, buildCronTrigger(scheduledRun.getCronExpression()));
    inMemoryRuns.put(scheduledRun.getId(), scheduledTask);
    log.info("Scheduled run with cron: {} and id: {}", scheduledRun.getCronExpression(), scheduledRun.getId());
  }

  private ScheduledRun persistIfNeeded(ScheduledRunRequest runRequest) {
    var existingEntity = scheduledRunRepository.findByCronExpressionAndAggregatedRunRequest(runRequest.cronExpression(), runRequest.aggregatedRunRequest());

    if (existingEntity.isPresent()) {
      return existingEntity.get();
    }

    var scheduledRun = ScheduledRun.builder()
        .cronExpression(runRequest.cronExpression())
        .aggregatedRunRequest(runRequest.aggregatedRunRequest())
        .build();

    log.info("Persisting scheduled run with cron: {}", scheduledRun.getCronExpression());
    return scheduledRunRepository.save(scheduledRun);
  }

  private void clearMemory() {
    this.inMemoryRuns.clear();
  }

  private void syncDatabaseAndMemory() {
    clearMemory();
    var databaseRuns = scheduledRunRepository.findAll();
    databaseRuns.forEach(this::schedule);
  }

  private static CronTrigger buildCronTrigger(String cronExpression) {
    return new CronTrigger(cronExpression, Clock.systemDefaultZone().getZone());
  }
}
