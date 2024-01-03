package com.kcs.aggregated;

import java.time.LocalDateTime;

public record AggregatedScanRunDto(String id, LocalDateTime date, String scoreRunId, String trivyRunId, ScanRunStatus status) {
}
