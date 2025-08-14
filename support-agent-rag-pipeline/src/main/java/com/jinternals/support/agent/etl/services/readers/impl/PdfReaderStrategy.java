package com.jinternals.support.agent.etl.services.readers.impl;

import com.jinternals.support.agent.etl.services.readers.ReaderStrategy;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;

import java.util.List;

public class PdfReaderStrategy implements ReaderStrategy {

    @Override
    public boolean supports(String extension, String contentType) {
        return "pdf".equalsIgnoreCase(extension) ||
                (contentType != null && contentType.toLowerCase().contains("pdf"));
    }

    @Override
    public List<Document> read(Resource resource) {
        try {
            return new PagePdfDocumentReader(resource).get();
        } catch (Exception e) {
            return new TikaDocumentReader(resource).get();
        }
    }
}
