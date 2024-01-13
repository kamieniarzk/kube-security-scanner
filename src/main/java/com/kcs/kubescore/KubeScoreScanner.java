package com.kcs.kubescore;

interface KubeScoreScanner {
  String score(String namespace, String additionalFlags);
  String scoreAllNamespaces(String additionalFlags);
}
