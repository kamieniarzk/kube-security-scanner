package com.kcs.scheduling;

import com.kcs.workload.AggregatedRunRequest;

public record ScheduledRunRequest(String cronExpression, AggregatedRunRequest aggregatedRunRequest) {
}
