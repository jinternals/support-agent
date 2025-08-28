package com.jinternals.support.agent.etl.services.readers.impl;

import com.jinternals.support.agent.etl.services.readers.Reader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.core.io.Resource;

import java.util.List;

@Slf4j
public class HtmlReader implements Reader {

    @Override
    public boolean supports(String extension, String contentType) {
        return "html".equalsIgnoreCase(extension) ||
                "htm".equalsIgnoreCase(extension) ||
                (contentType != null && contentType.toLowerCase().contains("html"));
    }

    @Override
    public List<Document> read(Resource resource) {
        log.info("Start extracting html document from {}", resource);
        JsoupDocumentReaderConfig config =  JsoupDocumentReaderConfig.builder()
                .includeLinkUrls(true)
                .build();
        return new JsoupDocumentReader(resource, config).get();
    }
}
