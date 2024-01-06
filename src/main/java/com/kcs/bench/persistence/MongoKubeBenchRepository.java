package com.kcs.bench.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class MongoKubeBenchRepository implements KubeBenchRepository {

  private final KubeBenchRunDocumentRepository documentRepository;

  @Override
  public KubeBenchRunDto save(KubeBenchRunCreate jobRunCreate) {
    return documentRepository.save(DocumentFactory.create(jobRunCreate)).toDto();
  }

  @Override
  public KubeBenchRunDto get(String id) {
    return documentRepository.findById(id).orElseThrow().toDto();
  }

  @Override
  public List<KubeBenchRunDto> getAll() {
    return documentRepository.findAll().stream().map(KubeBenchRunDocument::toDto).toList();
  }

  @Override
  public KubeBenchRunDto getMostRecentRun() {
    return documentRepository.findSortedByDate().stream().findFirst().orElseThrow().toDto();
  }

  @Override
  public List<KubeBenchRunDto> getAllWithoutStoredLogs() {
    return documentRepository.findWhereLogsStoredNullOrFalse().stream().map(KubeBenchRunDocument::toDto).toList();
  }

  @Override
  public void updateLogsStored(String id, Boolean logsStored) {
    var runDocument = documentRepository.findById(id).orElseThrow();
    runDocument.setLogsStored(logsStored);
    documentRepository.save(runDocument);
  }
}
