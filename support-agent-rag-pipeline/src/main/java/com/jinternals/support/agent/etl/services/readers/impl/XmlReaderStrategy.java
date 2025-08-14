package com.jinternals.support.agent.etl.services.readers.impl;

import com.jinternals.support.agent.etl.services.readers.ReaderStrategy;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;

import java.util.List;

public class XmlReaderStrategy implements ReaderStrategy {

    @Override
    public boolean supports(String extension, String contentType) {
        return "xml".equalsIgnoreCase(extension) ||
                (contentType != null && contentType.toLowerCase().contains("xml"));
    }

    @Override
    public List<Document> read(Resource resource) {
        return new TikaDocumentReader(resource).get();
    }
}