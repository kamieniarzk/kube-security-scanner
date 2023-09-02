package com.kcs.job;

import java.time.LocalDateTime;

public record JobRunDto(String id, LocalDateTime date, String podName) { }
