package com.jinternals.support.agent.etl.configuration;

import com.jinternals.support.agent.etl.services.tranformers.LowercaseKeywordsTransformer;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class DocumentTransformerConfiguration {

    @Bean
    @Order(10)
    public TextSplitter textSplitter() {
        return TokenTextSplitter.builder().build();
    }

    @Bean
    @Order(20)
    public KeywordMetadataEnricher keywordMetadataEnricher(ChatModel chatModel) {
        return new KeywordMetadataEnricher(chatModel, 5);
    }


    @Bean
    @Order(30)
    public LowercaseKeywordsTransformer lowercaseKeywordsTransformer() {
        return new LowercaseKeywordsTransformer();
    }


}
