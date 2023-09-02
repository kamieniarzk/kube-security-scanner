package com.kcs.job;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class MongoJobRunRepository implements JobRunRepository {

  private final JobRunDocumentRepository documentRepository;

  @Override
  public JobRunDto save(JobRunCreate jobRunCreate) {
    return documentRepository.save(DocumentFactory.create(jobRunCreate)).toDto();
  }

  @Override
  public JobRunDto get(String id) {
    return documentRepository.findById(id).orElseThrow().toDto();
  }

  @Override
  public List<JobRunDto> getAll() {
    return documentRepository.findAll().stream().map(JobRunDocument::toDto).toList();
  }

  @Override
  public List<JobRunDto> getMostRecent() {
    return documentRepository.findSortedByDate().stream().map(JobRunDocument::toDto).toList();
  }
}
