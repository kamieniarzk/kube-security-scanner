package com.kcs.aggregated.persistence;

import com.kcs.NoDataFoundException;
import com.kcs.aggregated.AggregatedScanRunDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class MongoAggregatedRunRepository implements AggregatedRunRepository {

  private final DocumentRepository documentRepository;

  @Override
  public AggregatedScanRunDto save(String scoreId, String trivyId) {
    var document = DocumentFactory.create(scoreId, trivyId);
    return documentRepository.save(document).toDto();
  }

  @Override
  public AggregatedScanRunDto get(String id) {
    return documentRepository.findById(id)
        .map(Document::toDto)
        .orElseThrow(NoDataFoundException::new);
  }
}
