package com.kcs.score;

interface KubeScoreRunner {
  String score(String namespace, String additionalFlags);
  String scoreAllNamespaces(String additionalFlags);
}
