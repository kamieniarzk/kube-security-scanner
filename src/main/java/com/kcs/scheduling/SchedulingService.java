package com.kcs.scheduling;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kcs.bench.persistence.log.LogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
class SchedulingService {

  private final LogService logService;

  @Scheduled(cron = "@hourly")
  void persistLogsForRunsWithoutStoredLogs() {
    log.info("Scheduling - persist logs for runs without stored logs");
    logService.persistRunLogsForRunsWithoutStoredLogs();
  }
}
