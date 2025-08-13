package com.jinternals.support.agent.etl.configuration;

import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipelineConfiguration {

    @Bean
    public TextSplitter textSplitter() {
        return TokenTextSplitter.builder().build();
    }

}
