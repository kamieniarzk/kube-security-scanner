package com.kcs.job;

public interface JobService {
  JobDto runJobFromUrl(String url, String podNamePrefix);
  JobDto runJobFromUrlDefinitionWithModifiedCommand(String url, String podNamePrefix, String command);
  JobDto runJobFromUrlDefinitionWithContainerArgs(String url, String namePrefix, String args);
  JobDto runWithContextServiceAccount(String yaml, String podNamePrefix);
  JobDto run(String yaml, String podNamePrefix);
  JobDto runWithArgs(String yaml, String podNamePrefix, String args);
}
