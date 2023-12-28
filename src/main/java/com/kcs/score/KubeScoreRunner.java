package com.kcs.score;

interface KubeScoreRunner {
  String score(String namespace);
  String scoreAllNamespaces();
}
