package com.jinternals.support.agent.vector;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class VectorDataLoaderService {

    @Value("classpath:/kb-articles.json")
    private Resource resource;
    private VectorStore vectorStore;


   @PostConstruct
    public void init() {
       JsonReader jsonReader = new JsonReader(this.resource, "id", "title", "category", "tags", "content", "url");
       List<Document> documents = jsonReader.get();
       this.vectorStore.add(documents);
   }

}
