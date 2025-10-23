package com.jinternals.support.agent.etl.services.readers;

import com.jinternals.support.agent.etl.services.readers.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.net.URLConnection;
import java.util.List;

import static org.apache.commons.io.FilenameUtils.getExtension;

@Service
@RequiredArgsConstructor
public class DocumentReaderService {
    private final List<Reader> readers;

    public List<Document> readFrom(Resource resource) {
        try {
            String filename = resource.getFilename();
            String ext = getExtension(filename);
            String contentType = detectContentType(resource);

            return readers.stream()
                    .filter(s -> s.supports(ext, contentType))
                    .findFirst()
                    .orElse(new TikaFallbackReader())
                    .read(resource);

        } catch (Exception e) {
            throw new RuntimeException("Failed to read document from URL: " + resource, e);
        }
    }


    private String detectContentType(Resource resource) {
        try {
            return URLConnection.guessContentTypeFromStream(resource.getInputStream());
        } catch (Exception e) {
            return null;
        }
    }
}
