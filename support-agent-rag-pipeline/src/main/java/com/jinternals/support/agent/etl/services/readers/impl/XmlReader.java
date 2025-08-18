package com.jinternals.support.agent.etl.services.readers.impl;

import com.jinternals.support.agent.etl.services.readers.Reader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;

import java.util.List;

@Slf4j
public class XmlReader implements Reader {

    @Override
    public boolean supports(String extension, String contentType) {
        return "xml".equalsIgnoreCase(extension) ||
                (contentType != null && contentType.toLowerCase().contains("xml"));
    }

    @Override
    public List<Document> read(Resource resource) {
        log.info("Start extracting xml document from {}", resource);
        return new TikaDocumentReader(resource).get();
    }
}