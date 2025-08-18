package com.jinternals.support.agent.etl.services.tranformers;


import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

public class LowercaseKeywordsTransformer implements DocumentTransformer {

    // Default key used by KeywordMetadataEnricher; change if you customized it
    private static final String EXCERPT_KEYWORDS_METADATA_KEY = "excerpt_keywords";

    @Override
    public List<Document> apply(List<Document> docs) {
        return docs.stream().map(d -> {
            Object val = d.getMetadata().get(EXCERPT_KEYWORDS_METADATA_KEY);
            if (val == null) return d;

            String lowered = toLowercaseCsv(val);
            if (lowered == null) return d;

            Map<String, Object> meta = new HashMap<>(d.getMetadata());
            meta.put(EXCERPT_KEYWORDS_METADATA_KEY, lowered);
            return new Document(d.getText(), meta);
        }).toList();
    }

    private static String toLowercaseCsv(Object val) {
        if (val instanceof String s) {
            var set = Arrays.stream(s.split("\\s*,\\s*"))
                    .map(String::trim)
                    .filter(k -> !k.isEmpty())
                    .map(String::toLowerCase)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            return String.join(", ", set);
        }
        if (val instanceof Collection<?> c) {
            var set = c.stream()
                    .map(Objects::toString)
                    .map(String::trim)
                    .filter(k -> !k.isEmpty())
                    .map(String::toLowerCase)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            return String.join(", ", set);
        }
        return null;
    }
}
