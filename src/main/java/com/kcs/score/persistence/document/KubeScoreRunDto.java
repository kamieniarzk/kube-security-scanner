package com.kcs.score.persistence.document;

import java.time.LocalDateTime;

public record KubeScoreRunDto(String id, LocalDateTime date, String namespace) {}
