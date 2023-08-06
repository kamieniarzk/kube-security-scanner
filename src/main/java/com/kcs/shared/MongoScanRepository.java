package com.kcs.shared;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kcs.NoDataFoundException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class MongoScanRepository implements ScanRepository {

  private final ScanDocumentRepository documentRepository;

  @Override
  public String save(final ScanRunCreate scanRunCreate) {
    return documentRepository.save(ScanRunDocumentFactory.createDocument(scanRunCreate)).getId();
  }

  @Override
  public ScanRun get(final String id) {
    return documentRepository.findById(id)
        .map(ScanRunDocument::toScanRun)
        .orElseThrow(NoDataFoundException::new);
  }

  @Override
  public List<ScanRun> getAll() {
    return documentRepository.findAll().stream()
        .map(ScanRunDocument::toScanRun)
        .toList();
  }

  @Override
  public ScanRun getMostRecentScanByType(final ScanType type) {
    return documentRepository.findByTypeSortedByDateAsc(type)
        .stream().findFirst()
        .orElseThrow(NoDataFoundException::new)
        .toScanRun();
  }

  @Override
  public List<ScanRun> getAllWithoutStoredLogs() {
    return documentRepository.findWhereLogsStoredNullOrFalse().stream()
        .map(ScanRunDocument::toScanRun)
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
