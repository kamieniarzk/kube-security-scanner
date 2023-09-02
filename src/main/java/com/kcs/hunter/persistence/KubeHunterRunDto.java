package com.kcs.hunter.persistence;

import java.time.LocalDateTime;

public record KubeHunterRunDto(String id, LocalDateTime date, String args, String jobRunId, Boolean logsStored) { }
