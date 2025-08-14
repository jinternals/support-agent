package com.jinternals.support.agent.etl.services.readers;

import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

import java.util.List;

public interface ReaderStrategy {
    boolean supports(String extension, String contentType);
    List<Document> read(Resource resource);
}
