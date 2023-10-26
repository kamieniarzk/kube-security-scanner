package com.kcs.job;

public interface JobService {
  JobRunDto runJobFromUrlDefinitionWithModifiedCommand(String url, String podNamePrefix, String command);
  JobRunDto runJobFromUrlDefinitionWithContainerArgs(String url, String namePrefix, String args);
  JobRunDto runJobFromUrlDefinitionWithContextServiceAccount(String yaml, String podNamePrefix);
}
