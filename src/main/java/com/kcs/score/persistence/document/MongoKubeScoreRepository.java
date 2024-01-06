package com.kcs.score.persistence.document;

import com.kcs.NoDataFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class MongoKubeScoreRepository implements KubeScoreRepository {

  private final KubeScoreRunDocumentRepository documentRepository;

  @Override
  public String save(KubeScoreRunCreate runCreate) {
    return documentRepository.save(DocumentFactory.create(runCreate)).id();
  }

  @Override
  public List<KubeScoreRunDto> getByNamespace(String namespace) {
    return documentRepository.findByNamespace(namespace).stream()
        .map(KubeScoreRunDocument::toDto)
        .toList();
  }

  @Override
  public KubeScoreRunDto getById(String id) {
    return documentRepository.findById(id)
        .map(KubeScoreRunDocument::toDto)
        .orElseThrow(NoDataFoundException::new);
  }
}
