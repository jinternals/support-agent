package com.jinternals.support.agent.etl.services;

import com.jinternals.support.agent.etl.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.Filter.Expression;
import org.springframework.ai.vectorstore.filter.Filter.Key;
import org.springframework.ai.vectorstore.filter.Filter.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jinternals.support.agent.etl.Constants.KEY_SOURCE_PATH;
import static org.springframework.ai.vectorstore.filter.Filter.ExpressionType.*;

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

    public void delete(String sourcePath) {
        Expression filterExpression = new Expression(EQ, new Key(KEY_SOURCE_PATH), new Value(sourcePath));
        vectorStore.delete(filterExpression);
    }


}
