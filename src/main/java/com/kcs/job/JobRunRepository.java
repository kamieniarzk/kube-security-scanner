package com.kcs.job;

import java.util.List;

public interface JobRunRepository {
  JobRunDto save(JobRunCreate runCreate);
  JobRunDto get(String id);
  List<JobRunDto> getAll();
  List<JobRunDto> getMostRecent();
}
