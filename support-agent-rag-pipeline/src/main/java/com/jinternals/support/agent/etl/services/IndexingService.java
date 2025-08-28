package com.jinternals.support.agent.etl.services;

import com.jinternals.support.agent.etl.entities.VectorDocument;
import com.jinternals.support.agent.etl.repositories.VectorDocumentRepository;
import com.jinternals.support.agent.etl.services.readers.DocumentReader;
import com.jinternals.support.agent.etl.services.tranformers.DocumentTransformerService;
import com.jinternals.support.agent.etl.services.writers.DocumentWriterService;
import com.jinternals.support.agent.etl.utils.HashUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jinternals.support.agent.etl.constants.Constants.KEY_SOURCE_PATH;
import static org.springframework.core.io.UrlResource.from;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
@AllArgsConstructor
public class IndexingService {

    private VectorDocumentRepository vectorDocumentRepository;
    private DocumentReader documentReader;
    private DocumentWriterService documentWriterService;
    private DocumentTransformerService documentTransformerService;

    @Transactional
    public List<Document> indexDocumentFromURL(String sourcePath, Map<String,String> metadata, boolean reIndex) {
        log.info("Indexing document from URL: {} and {}", sourcePath, metadata);
        return processDocument(from(sourcePath), metadata, reIndex);
    }

    private void addCustomMetadata(Document document, String sourcePath, Map<String,String> metadata) {
        notNull(document, "Document must not be null");

        document.getMetadata().put(KEY_SOURCE_PATH, sourcePath);

        if (!isEmpty(metadata)) {
            document.getMetadata().putAll(metadata);
        }
    }


    @SneakyThrows
    private List<Document> processDocument(Resource resource, Map<String,String> metadata, boolean reIndex) {
        isTrue(resource != null && resource.exists(), "Resource must not be null and must exist");

        var sourcePath = resource.getURI().toString();
        var existingVectorDocument = vectorDocumentRepository.findBySourcePath(sourcePath);
        var resourceHash = HashUtils.calculateHash(resource);

        if(isDocumentAlreadyIndexed(existingVectorDocument, resourceHash) && !reIndex) {
            log.info("Document already indexed, skipping indexing");
            return List.of();
        }

        if(reIndex) {
            log.info("Deleting existing index for document: {}", sourcePath);
            documentWriterService.delete(sourcePath);
        }

        var vectorDocument = existingVectorDocument.orElse(VectorDocument.builder()
                .sourcePath(sourcePath)
                .hash(resourceHash)
                .build());

        var parsedDocuments = documentReader.readFrom(resource);
        var chunkedDocuments = documentTransformerService.transform(parsedDocuments);

        chunkedDocuments.forEach(doc -> addCustomMetadata(doc, sourcePath, metadata));

        documentWriterService.write(chunkedDocuments);

        vectorDocument.setHash(resourceHash);
        vectorDocumentRepository.save(vectorDocument);

        return chunkedDocuments;
    }

    private static boolean isDocumentAlreadyIndexed(Optional<VectorDocument> existingFromDb, String resourceHash) {
        return existingFromDb.isPresent() && resourceHash.equals(existingFromDb.get().getHash());
    }


}
