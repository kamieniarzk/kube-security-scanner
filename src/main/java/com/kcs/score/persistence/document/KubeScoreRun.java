package com.kcs.score.persistence.document;

import java.time.LocalDateTime;

public record KubeScoreRun(String id, LocalDateTime date, String namespace) {}
