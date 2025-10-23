package com.jinternals.support.agent.etl.services;

import com.jinternals.support.agent.etl.entities.VectorDocument;
import com.jinternals.support.agent.etl.repositories.VectorDocumentRepository;
import com.jinternals.support.agent.etl.services.readers.DocumentReaderService;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

    private static final String CUSTOM_KEYWORDS_METADATA_KEY = "custom_keywords";

    private VectorDocumentRepository vectorDocumentRepository;
    private DocumentReaderService documentReaderService;
    private DocumentTransformerService documentTransformerService;
    private DocumentWriterService documentWriterService;

    @Transactional
    public List<Document> indexDocumentFromURL(String sourcePath, Map<String, Object> metadata, boolean reIndex) {
        notNull(sourcePath, "Document must not be null");
        log.info("Indexing document from URL: {} and {}", sourcePath, metadata);
        return processDocument(from(sourcePath), metadata, reIndex);
    }

    private void addCustomMetadata(Document document, String sourcePath, Map<String, Object> metadata) {
        notNull(document, "Document must not be null");

        document.getMetadata().put(KEY_SOURCE_PATH, sourcePath);

        if (!isEmpty(metadata)) {
            document.getMetadata().putAll(metadata);
        }
    }


    @SneakyThrows
    private List<Document> processDocument(Resource resource,  Map<String, Object> metadata, boolean reIndex) {
        isTrue(resource != null && resource.exists(), "Resource must not be null and must exist");

        var cleanSourcePath = cleanSourcePath(resource);
        var existingVectorDocument = vectorDocumentRepository.findBySourcePath(cleanSourcePath);
        var resourceHash = HashUtils.calculateHash(cleanSourcePath, resource);

        if(shouldSkipIndexing(reIndex, existingVectorDocument, resourceHash)) {
            log.info("Document already indexed, skipping indexing");
            return List.of();
        }

        deleteExistingIndexIfNeeded(cleanSourcePath, existingVectorDocument, resourceHash, reIndex);

        var vectorDocument = getOrCreateVectorDocument(existingVectorDocument, cleanSourcePath, resourceHash);
        var chunkedDocuments = transformAndEnrichDocuments(resource, metadata, cleanSourcePath);

        persistDocuments(chunkedDocuments, vectorDocument, resourceHash);

        return chunkedDocuments;
    }

    private void deleteExistingIndex(String cleanSourcePath) {
        log.info("Deleting existing index for document: {}", cleanSourcePath);
        documentWriterService.delete(cleanSourcePath);
    }

    private void persistDocuments(List<Document> chunkedDocuments, VectorDocument vectorDocument, String resourceHash) {
        documentWriterService.write(chunkedDocuments);
        vectorDocument.setHash(resourceHash);
        vectorDocumentRepository.save(vectorDocument);
    }

    private List<Document> transformAndEnrichDocuments(Resource resource, Map<String, Object> metadata, String cleanSourcePath) {
        var parsedDocuments = documentReaderService.readFrom(resource);
        var chunkedDocuments = documentTransformerService.transform(parsedDocuments);

        chunkedDocuments.forEach(doc -> addCustomMetadata(doc, cleanSourcePath, metadata));
        return chunkedDocuments;
    }

    private static boolean shouldSkipIndexing(boolean reIndex, Optional<VectorDocument> existingVectorDocument, String resourceHash) {
        return isDocumentAlreadyIndexed(existingVectorDocument, resourceHash) && !reIndex;
    }

    private static VectorDocument getOrCreateVectorDocument(Optional<VectorDocument> existingVectorDocument, String cleanSourcePath, String resourceHash) {
        return existingVectorDocument.orElse(VectorDocument.builder()
                .sourcePath(cleanSourcePath)
                .hash(resourceHash)
                .build());
    }

    private static String cleanSourcePath(Resource resource) throws IOException, URISyntaxException {
        var uri = resource.getURI();
        var cleanUri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, null);
        return cleanUri.toString();
    }

    private static boolean isDocumentAlreadyIndexed(Optional<VectorDocument> existingFromDb, String resourceHash) {
        return existingFromDb.isPresent() && resourceHash.equals(existingFromDb.get().getHash());
    }

    private static boolean hasDocumentChanged(Optional<VectorDocument> existingFromDb, String newhash){
        return existingFromDb.isPresent() && !isDocumentAlreadyIndexed(existingFromDb, newhash);
    }
    private static boolean shouldDeleteBeforeWrite(Optional<VectorDocument> existingFromDb, String newHash, boolean reIndex) {
        return reIndex || hasDocumentChanged(existingFromDb, newHash);
    }
    private void deleteExistingIndexIfNeeded(String sourcePath, Optional<VectorDocument> existingFromDb, String newHash, boolean reIndex) {
        if (shouldDeleteBeforeWrite(existingFromDb, newHash, reIndex)) {
            String reason  = reIndex ? "reIndex=true" : "Hash changed";
            log.info("Delete-before-write for {} because {}", sourcePath, reason);
            deleteExistingIndex(sourcePath);
        } else {
            log.info("No delete needed for {} (unchanged & reIndex=false or no existing row)", sourcePath);
        }
    }

}
