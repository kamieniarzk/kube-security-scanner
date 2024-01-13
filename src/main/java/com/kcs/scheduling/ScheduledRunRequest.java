package com.kcs.scheduling;

import com.kcs.aggregated.AggregatedScanRequest;

public record ScheduledRunRequest(String cronExpression, AggregatedScanRequest aggregatedScanRequest) {
}
