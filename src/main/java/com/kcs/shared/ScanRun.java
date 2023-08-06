package com.kcs.shared;

import java.time.LocalDateTime;

public record ScanRun(String id, LocalDateTime date, ScanType type, String podName, Boolean logsStored) {}
