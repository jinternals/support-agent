package com.jinternals.support.agent.etl.services.readers.impl;

import com.jinternals.support.agent.etl.services.readers.Reader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

import java.util.List;

@Slf4j
public class JsonReader implements Reader {

    @Override
    public boolean supports(String extension, String contentType) {
        return "json".equalsIgnoreCase(extension) ||
                (contentType != null && contentType.toLowerCase().contains("json"));
    }

    @Override
    public List<Document> read(Resource resource) {
        log.info("Start extracting json document from {}", resource);

        return new org.springframework.ai.reader.JsonReader(resource).get();
    }
}
