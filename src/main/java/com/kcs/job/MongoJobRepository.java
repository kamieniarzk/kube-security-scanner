package com.kcs.job;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class MongoJobRepository implements JobRepository {

  private final JobDocumentRepository documentRepository;

  @Override
  public JobDto save(JobCreate jobCreate) {
    return documentRepository.save(DocumentFactory.create(jobCreate)).toDto();
  }

  @Override
  public JobDto get(String id) {
    return documentRepository.findById(id).orElseThrow().toDto();
  }

  @Override
  public List<JobDto> getAll() {
    return documentRepository.findAll().stream().map(JobDocument::toDto).toList();
  }
}
