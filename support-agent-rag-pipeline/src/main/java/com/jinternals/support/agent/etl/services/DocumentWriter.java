package com.jinternals.support.agent.etl.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentWriter {
    private final VectorStore vectorStore;

    public void write(List<Document> documents) {
       documents.forEach(doc ->
               log.info("Document: id {} length {}", doc.getId(), doc.getText().length())
       );
       vectorStore.add(documents);
    }
}
