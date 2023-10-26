package com.kcs.trivy.persistence;

import java.time.LocalDateTime;

public record TrivyRunDto(String id, LocalDateTime date, String jobRunId, Boolean logsStored) { }
