package com.jinternals.support.configuration;

import com.jinternals.support.tools.MeetingTools;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpServerFeatures.SyncPromptSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.Prompt;
import io.modelcontextprotocol.spec.McpSchema.PromptArgument;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MethodToolConfiguration {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(MeetingTools meetingTools) {
       return MethodToolCallbackProvider
               .builder()
               .toolObjects(meetingTools)
               .build();
    }





}
