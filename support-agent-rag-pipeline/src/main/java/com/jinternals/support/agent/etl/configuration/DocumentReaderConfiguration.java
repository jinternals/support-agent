package com.jinternals.support.agent.etl.configuration;

import com.jinternals.support.agent.etl.services.readers.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class DocumentReaderConfiguration {

    @Bean
    @Order(10)
    public PdfReader pdfReaderStrategy() {
        return new PdfReader();
    }

    @Bean
    @Order(20)
    public MarkdownReader markdownReaderStrategy() {
        return new MarkdownReader();
    }

    @Bean
    @Order(30)
    public HtmlReader htmlReaderStrategy() {
        return new HtmlReader();
    }

    @Bean
    @Order(40)
    public JsonReader jsonReaderStrategy() {
        return new JsonReader();
    }

    @Bean
    @Order(50)
    public XmlReader xmlReaderStrategy() {
        return new XmlReader();
    }


}
