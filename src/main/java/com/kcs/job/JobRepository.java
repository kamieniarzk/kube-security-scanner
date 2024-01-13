package com.kcs.job;

import java.util.List;

public interface JobRepository {
  JobDto save(JobCreate runCreate);
  JobDto get(String id);
  List<JobDto> getAll();
}
