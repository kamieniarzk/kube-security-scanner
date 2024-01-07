package com.kcs.scheduling;

import com.kcs.NoDataFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

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

  String schedule(ScheduledRun scheduledRun) {
    var runnable = runFactory.create(scheduledRun.getRunType());
    var scheduledTask = taskScheduler.schedule(runnable, scheduledRun.getCronTrigger());
    var persistedScheduledRun = persistIfNeeded(scheduledRun);
    inMemoryRuns.put(persistedScheduledRun.getId(), scheduledTask);
    log.info("Scheduled run with cron: {} and id: {}", scheduledRun.getCronExpression(), persistedScheduledRun.getId());
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

  private ScheduledRun persistIfNeeded(ScheduledRun scheduledRun) {
    var id = scheduledRun.getId();

    if (id == null) {
      return persist(scheduledRun);
    }

    if (!scheduledRunRepository.existsById(id)) {
      return persist(scheduledRun);
    }

    return scheduledRun;
  }

  private ScheduledRun persist(ScheduledRun scheduledRun) {
    log.info("Persisting scheduled {} run with cron: {}", scheduledRun.getRunType(), scheduledRun.getCronExpression());
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
}
