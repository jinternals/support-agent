package com.jinternals.support.agent.etl.services.readers.impl;

import com.jinternals.support.agent.etl.services.readers.ReaderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;

import java.util.List;

@Slf4j
public class TikaFallbackReaderStrategy implements ReaderStrategy {

    @Override
    public boolean supports(String extension, String contentType) {
        return true;
    }

    @Override
    public List<Document> read(Resource resource) {
        log.info("Start extracting document using fallback reader from {}", resource);
        return new TikaDocumentReader(resource).get();
    }
}