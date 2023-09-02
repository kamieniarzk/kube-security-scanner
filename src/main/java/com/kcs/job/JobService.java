package com.kcs.job;

public interface JobService {
  JobRunDto runJobFromUrlDefinition(String url, String podNamePrefix);
  JobRunDto runJobFromUrlDefinitionWithContainerArgs(String url, String namePrefix, String args);
}
