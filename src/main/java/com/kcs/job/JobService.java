package com.kcs.job;

public interface JobService {
  JobDto runJobFromUrlDefinitionWithModifiedCommand(String url, String podNamePrefix, String command);
  JobDto runJobFromUrlDefinitionWithContainerArgs(String url, String namePrefix, String args);
  JobDto runJobFromUrlDefinitionWithContextServiceAccount(String yaml, String podNamePrefix);
}
