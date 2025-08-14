package com.jinternals.support.agent.etl.services.readers.impl;

import com.jinternals.support.agent.etl.services.readers.ReaderStrategy;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.core.io.Resource;

import java.util.List;

public class HtmlReaderStrategy implements ReaderStrategy {

    @Override
    public boolean supports(String extension, String contentType) {
        return "html".equalsIgnoreCase(extension) ||
                "htm".equalsIgnoreCase(extension) ||
                (contentType != null && contentType.toLowerCase().contains("html"));
    }

    @Override
    public List<Document> read(Resource resource) {
        return new JsoupDocumentReader(resource).get();
    }
}
