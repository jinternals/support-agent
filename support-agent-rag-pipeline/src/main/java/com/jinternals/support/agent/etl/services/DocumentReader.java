package com.jinternals.support.agent.etl.services;

import com.jinternals.support.agent.etl.services.readers.ReaderStrategy;
import com.jinternals.support.agent.etl.services.readers.impl.*;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.io.FilenameUtils.getExtension;

@Service
public class DocumentReader {

    private final List<ReaderStrategy> strategies = Arrays.asList(
            new PdfReaderStrategy(),
            new HtmlReaderStrategy(),
            new JsonReaderStrategy(),
            new XmlReaderStrategy()
    );

    public List<Document> readFrom(Resource resource) {
        try {
            String filename = resource.getFilename();
            String ext = getExtension(filename);
            String contentType = detectContentType(resource);

            return strategies.stream()
                    .filter(s -> s.supports(ext, contentType))
                    .findFirst()
                    .orElse(new TikaFallbackReaderStrategy())
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
