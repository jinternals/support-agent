package com.jinternals.support.agent.services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonMetadataGenerator;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
public class VectorDataLoaderService {

    @Value("classpath:/data/kb-articles.json")
    private Resource resource;
    private VectorStore vectorStore;


   @PostConstruct
    public void init() {
       JsonReader jsonReader = new JsonReader(this.resource, jsonMap -> Map.of(
               "tags",jsonMap.get("tags"), "client","client1"),
               "id", "title", "category", "content", "url");
       List<Document> documents = jsonReader.get();
       this.vectorStore.add(documents);
   }

}
