package com.kcs.bench.persistence.dto;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kcs.NoDataFoundException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class MongoKubeBenchRepository implements KubeBenchRepository {

  private final KubeBenchRunDocumentRepository documentRepository;

  @Override
  public String save(final KubeBenchRunCreate kubeBenchRunCreate) {
    return documentRepository.save(ScanRunDocumentFactory.createDocument(kubeBenchRunCreate)).getId();
  }

  @Override
  public KubeBenchRun get(final String id) {
    return documentRepository.findById(id)
        .map(KubeBenchRunDocument::toScanRun)
        .orElseThrow(NoDataFoundException::new);
  }

  @Override
  public List<KubeBenchRun> getAll() {
    return documentRepository.findAll().stream()
        .map(KubeBenchRunDocument::toScanRun)
        .toList();
  }

  @Override
  public KubeBenchRun getMostRecentRun() {
    return documentRepository.findSortedByDate()
        .stream().findFirst()
        .orElseThrow(NoDataFoundException::new)
        .toScanRun();
  }

  @Override
  public List<KubeBenchRun> getAllWithoutStoredLogs() {
    return documentRepository.findWhereLogsStoredNullOrFalse().stream()
        .map(KubeBenchRunDocument::toScanRun)
        .toList();
  }

  @Override
  public void updateLogsStored(final String id, final Boolean logsStored) {
    var existingDocument = documentRepository.findById(id);
    if (existingDocument.isPresent()) {
      var document = existingDocument.get();
      document.setLogsStored(logsStored);
      documentRepository.save(document);
    } else {
      throw new NoDataFoundException();
    }
  }
}
