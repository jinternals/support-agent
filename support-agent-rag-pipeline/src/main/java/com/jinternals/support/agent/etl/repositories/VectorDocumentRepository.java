package com.jinternals.support.agent.etl.repositories;

import com.jinternals.support.agent.etl.entities.VectorDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VectorDocumentRepository extends JpaRepository<VectorDocument, UUID> {

    Optional<VectorDocument> findBySourcePath(String sourcePath);

}

