package com.kcs.shared;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.kcs.NoDataFoundException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class MongoScanRepository implements ScanRepository {
  private final ScanDocumentRepository documentRepository;

  @Override
  public String save(final ScanRun scanRun) {
    return documentRepository.save(ScanRunDocumentFactory.createDocument(scanRun)).id();
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
}
