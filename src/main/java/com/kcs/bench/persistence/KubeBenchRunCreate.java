package com.kcs.bench.persistence;

import com.kcs.bench.dto.KubeBenchTarget;

public record KubeBenchRunCreate(String jobRunId, KubeBenchTarget target) {}
