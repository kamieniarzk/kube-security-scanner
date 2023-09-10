package com.kcs.hunter.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
class MongoKubeHunterRepository implements KubeHunterRepository {

  private final KubeHunterRunDocumentRepository documentRepository;

  @Override
  public KubeHunterRunDto save(KubeHunterRunCreate runCreate) {
    return documentRepository.save(DocumentFactory.create(runCreate)).toDto();
  }

  @Override
  public KubeHunterRunDto get(String id) {
    return documentRepository.findById(id).orElseThrow().toDto();
  }

  @Override
  public List<KubeHunterRunDto> getAll() {
    return documentRepository.findAll().stream().map(KubeHunterRunDocument::toDto).toList();
  }

  @Override
  public KubeHunterRunDto getMostRecentRun() {
    return documentRepository.findSortedByDate().stream().findFirst().orElseThrow().toDto();
  }

  @Override
  public List<KubeHunterRunDto> getAllWithoutStoredLogs() {
    return documentRepository.findWhereLogsStoredNullOrFalse().stream().map(KubeHunterRunDocument::toDto).toList();
  }

  @Override
  @Transactional
  public void updateLogsStored(String id, Boolean logsStored) {
    var runDocument = documentRepository.findById(id).orElseThrow();
    runDocument.setLogsStored(logsStored);
    documentRepository.save(runDocument);
  }
}
