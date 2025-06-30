package com.jinternals.support.configuration;

import com.jinternals.support.tools.SupportTicketTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MethodToolConfiguration {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(SupportTicketTool supportTicketTool) {
       return MethodToolCallbackProvider
               .builder()
               .toolObjects(supportTicketTool)
               .build();
    }





}
