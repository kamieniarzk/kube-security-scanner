package com.kcs.bench.dto;

import java.time.LocalDateTime;

public record KubeBenchRunDto(String id, LocalDateTime date, String jobRunId, Boolean logsStored, KubeBenchTarget target) {}
