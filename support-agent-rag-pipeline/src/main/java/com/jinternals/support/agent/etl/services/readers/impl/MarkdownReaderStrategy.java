package com.jinternals.support.agent.etl.services.readers.impl;

import com.jinternals.support.agent.etl.services.readers.ReaderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;

import java.util.List;

@Slf4j
public class MarkdownReaderStrategy implements ReaderStrategy {

    @Override
    public boolean supports(String extension, String contentType) {
        return "md".equalsIgnoreCase(extension) ||
                (contentType != null && contentType.toLowerCase().contains("markdown"));
    }

    @Override
    public List<Document> read(Resource resource) {
        log.info("Start extracting markdown document from {}", resource);

        MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                .withIncludeBlockquote(true)
                .withHorizontalRuleCreateDocument(true)
                .withIncludeCodeBlock(true)
                .build();
        return new MarkdownDocumentReader(resource, config).get();
    }
}
