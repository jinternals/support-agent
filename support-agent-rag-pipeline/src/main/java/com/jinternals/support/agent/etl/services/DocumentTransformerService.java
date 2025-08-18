package com.jinternals.support.agent.etl.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DocumentTransformerService {

    private final List<DocumentTransformer> pipeline; // ordered

    public DocumentTransformerService(List<DocumentTransformer> transformers) {
        this.pipeline = List.copyOf(transformers);
    }

    public List<Document> transform(List<Document> input) {
        List<Document> out = input;
        for (var step : pipeline) {
            log.info("Executing step {}", step);
            out = step.apply(out);
        }
        return out;
    }

}
