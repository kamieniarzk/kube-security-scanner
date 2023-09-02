package com.kcs.bench.persistence;

import java.time.LocalDateTime;

public record KubeBenchRunDto(String id, LocalDateTime date, String jobRunId, Boolean logsStored) {}
