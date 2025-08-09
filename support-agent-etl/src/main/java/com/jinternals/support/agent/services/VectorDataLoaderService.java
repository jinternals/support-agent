package com.jinternals.support.agent.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.String.valueOf;


@Slf4j
@Service
@AllArgsConstructor
public class VectorDataLoaderService {

    private static final String CONTEXT = "support";

    @Value("classpath:/documents/kb-articles.json") // move JSON
    private Resource kbArticlesJson;
    private VectorStore vectorStore;
    private ObjectMapper objectMapper;

    @SneakyThrows
    @PostConstruct
    public void init() {
//
//        String jsonContent = StreamUtils.copyToString(kbArticlesJson.getInputStream(), StandardCharsets.UTF_8);
//        List<Map<String, Object>> rawArticles = objectMapper.readValue(jsonContent, new TypeReference<>() {
//        });
//
//        List<Document> documents = rawArticles.stream().map(article -> {
//            Map<String, Object> metadata = new HashMap<>();
//            metadata.put("title", article.get("title"));
//            metadata.put("category", article.get("category"));
//            metadata.put("url", article.get("url"));
//            metadata.put("tags", article.get("tags"));
//            metadata.put("context", CONTEXT);
//
//
//
//
//            return new Document(valueOf(article.get("id")), valueOf(article.get("content")), metadata);
//        }).toList();
//
//        // Step 3: Delete existing IDs (optional)
//        List<String> ids = documents.stream().map(Document::getId).toList();
//        log.info("Deleting existing documents: {}" , ids.size());
//        vectorStore.delete(ids);
//
//        // Step 4: Add new documents
//        log.info("Inserting documents: {}"  , documents.size());
//        vectorStore.add(documents);


        String jsonContent = StreamUtils.copyToString(kbArticlesJson.getInputStream(), StandardCharsets.UTF_8);
        List<Map<String, Object>> rawArticles = objectMapper.readValue(jsonContent, new TypeReference<>() {});

        List<Document> documents = rawArticles.stream().map(article -> {
            String id       = valueOf(article.getOrDefault("id", UUID.randomUUID().toString()));
            String title    = trimToEmpty(article.get("title"));
            String category = trimToEmpty(article.get("category"));
            String url      = trimToEmpty(article.get("url"));
            String content  = trimToEmpty(article.get("content"));
            List<String> tags = parseTags(article.get("tags"));

            String textForEmbedding = """
                                      [%s]%s%s
                            
                                      %s
                                      """.formatted(
                                                title.isBlank() ? "Untitled" : title,
                                                category.isBlank() ? "" : " (" + category + ")",
                                                url.isBlank() ? "" : " | " + url,
                                                content
                                        ).trim();

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("title", title);
            metadata.put("category", category);
            metadata.put("url", url);
            metadata.put("tags", tags);
            metadata.put("context", CONTEXT);
            metadata.put("raw_content", content);

            return new Document(id, textForEmbedding, metadata);
        }).toList();

        List<String> ids = documents.stream().map(Document::getId).toList();
        log.info("Deleting existing documents: {}", ids.size());
        vectorStore.delete(ids);

        log.info("Inserting documents: {}", documents.size());
        vectorStore.add(documents);


    }

    private static String trimToEmpty(Object o) {
        return o == null ? "" : String.valueOf(o).trim();
    }

    @SuppressWarnings("unchecked")
    private static List<String> parseTags(Object value) {
        if (value == null) return List.of();
        if (value instanceof List<?> list) {
            return list.stream().map(String::valueOf).map(String::trim).filter(s -> !s.isEmpty()).toList();
        }
        // allow comma/semicolon string inputs
        String s = String.valueOf(value);
        return Arrays.stream(s.split("[,;]"))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .toList();
    }




}
