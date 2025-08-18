package com.jinternals.support.agent.etl.services;

import com.jinternals.support.agent.etl.entities.VectorDocument;
import com.jinternals.support.agent.etl.repositories.VectorDocumentRepository;
import com.jinternals.support.agent.etl.services.readers.DocumentReader;
import com.jinternals.support.agent.etl.services.tranformers.DocumentTransformerService;
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

import static com.jinternals.support.agent.etl.services.Constants.KEY_SOURCE_PATH;
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
    private DocumentReader documentReader;
    private DocumentWriter documentWriter;
    private DocumentTransformerService documentTransformerService;

    @Transactional
    public List<Document> indexDocumentFromURL(String sourcePath, List<String> keywords, boolean reIndex) {
        log.info("Indexing document from URL: {} and {}", sourcePath, keywords);
        return processDocument(from(sourcePath), keywords, reIndex);
    }

    private void addCustomMetadata(Document document, String sourcePath, List<String> keywords) {
        notNull(document, "Document must not be null");

        document.getMetadata().put(KEY_SOURCE_PATH, sourcePath);

        if (!isEmpty(keywords)) {
            document.getMetadata().putAll(Map.of(CUSTOM_KEYWORDS_METADATA_KEY, keywords));
        }
    }


    @SneakyThrows
    private List<Document> processDocument(Resource resource, List<String> keywords, boolean reIndex) {
        isTrue(resource != null && resource.exists(), "Resource must not be null and must exist");

        var sourcePath = resource.getURI().toString();
        var existingVectorDocument = vectorDocumentRepository.findBySourcePath(sourcePath);
        var resourceHash = HashUtils.calculateHash(resource);

        if(isDocumentAlreadIndexed(existingVectorDocument, resourceHash) && !reIndex) {
            log.info("Document already indexed, skipping indexing");
            return List.of();
        }

        if(reIndex) {
            log.info("Deleting existing index for document: {}", sourcePath);
            documentWriter.delete(sourcePath);
        }

        var vectorDocument = existingVectorDocument.orElse(VectorDocument.builder()
                .sourcePath(sourcePath)
                .hash(resourceHash)
                .build());

        var parsedDocuments = documentReader.readFrom(resource);
        var chunkedDocuments = documentTransformerService.transform(parsedDocuments);

        chunkedDocuments.forEach(doc -> addCustomMetadata(doc, sourcePath, keywords));

        documentWriter.write(chunkedDocuments);

        vectorDocument.setHash(resourceHash);
        vectorDocumentRepository.save(vectorDocument);

        return chunkedDocuments;
    }

    private static boolean isDocumentAlreadIndexed(Optional<VectorDocument> existingFromDb, String resourceHash) {
        return existingFromDb.isPresent() && resourceHash.equals(existingFromDb.get().getHash());
    }


}
