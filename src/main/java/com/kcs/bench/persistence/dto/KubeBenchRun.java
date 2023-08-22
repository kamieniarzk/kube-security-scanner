package com.kcs.bench.persistence.dto;

import java.time.LocalDateTime;

public record KubeBenchRun(String id, LocalDateTime date, String podName, Boolean logsStored) {}
