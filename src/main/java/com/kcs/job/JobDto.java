package com.kcs.job;

import java.time.LocalDateTime;

public record JobDto(String id, LocalDateTime date, String podName) { }
