package com.jinternals.support.agent.etl.services.readers.impl;

import com.jinternals.support.agent.etl.services.readers.ReaderStrategy;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.core.io.Resource;

import java.util.List;

public class JsonReaderStrategy implements ReaderStrategy {

    @Override
    public boolean supports(String extension, String contentType) {
        return "json".equalsIgnoreCase(extension) ||
                (contentType != null && contentType.toLowerCase().contains("json"));
    }

    @Override
    public List<Document> read(Resource resource) {
        return new JsonReader(resource).get();
    }
}
