package com.kcs.kubebench.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class MongoKubeBenchRepository implements KubeBenchRepository {

  private final KubeBenchScanDocumentRepository documentRepository;

  @Override
  public KubeBenchScanDto save(KubeBenchScanCreate jobRunCreate) {
    return documentRepository.save(DocumentFactory.create(jobRunCreate)).toDto();
  }

  @Override
  public KubeBenchScanDto get(String id) {
    return documentRepository.findById(id).orElseThrow().toDto();
  }

  @Override
  public List<KubeBenchScanDto> getAll() {
    return documentRepository.findAll().stream().map(KubeBenchScanDocument::toDto).toList();
  }

  @Override
  public KubeBenchScanDto getMostRecentRun() {
    return documentRepository.findSortedByDate().stream().findFirst().orElseThrow().toDto();
  }

  @Override
  public List<KubeBenchScanDto> getAllWithoutStoredLogs() {
    return documentRepository.findWhereLogsStoredNullOrFalse().stream().map(KubeBenchScanDocument::toDto).toList();
  }

  @Override
  public void updateLogsStored(String id, Boolean logsStored) {
    var runDocument = documentRepository.findById(id).orElseThrow();
    runDocument.setLogsStored(logsStored);
    documentRepository.save(runDocument);
  }
}
