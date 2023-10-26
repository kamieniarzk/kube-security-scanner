package com.kcs.trivy.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
class MongoTrivyRepository implements TrivyRepository {

  private final TrivyRunDocumentRepository documentRepository;

  @Override
  public TrivyRunDto save(TrivyRunCreate runCreate) {
    return documentRepository.save(DocumentFactory.create(runCreate)).toDto();
  }

  @Override
  public TrivyRunDto get(String id) {
    return documentRepository.findById(id).orElseThrow().toDto();
  }

  @Override
  public List<TrivyRunDto> getAll() {
    return documentRepository.findAll().stream().map(TrivyRunDocument::toDto).toList();
  }

  @Override
  public TrivyRunDto getMostRecentRun() {
    return documentRepository.findSortedByDate().stream().findFirst().orElseThrow().toDto();
  }

  @Override
  public List<TrivyRunDto> getAllWithoutStoredLogs() {
    return documentRepository.findWhereLogsStoredNullOrFalse().stream().map(TrivyRunDocument::toDto).toList();
  }

  @Override
  @Transactional
  public void updateLogsStored(String id, Boolean logsStored) {
    var runDocument = documentRepository.findById(id).orElseThrow();
    runDocument.setLogsStored(logsStored);
    documentRepository.save(runDocument);
  }
}
