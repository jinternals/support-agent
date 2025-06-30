package com.jinternals.support.agent.configurations;

import io.modelcontextprotocol.client.McpSyncClient;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
@AllArgsConstructor
public class AssistantAgentConfiguration {

    @Value("classpath:/prompts/system-prompt.txt")
    private Resource systemPromptResource;

    @Value("classpath:/prompts/system-prompt-kb.txt")
    private Resource systemPromptResourceKb;

    private List<McpSyncClient> mcpSyncClients;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder,
                                 SimpleLoggerAdvisor simpleLoggerAdvisor,
                                 MessageChatMemoryAdvisor messageChatMemoryAdvisor,
                                 QuestionAnswerAdvisor questionAnswerAdvisor
    ) {

        return builder
                .defaultSystem(systemPromptResourceKb)
                .defaultAdvisors(
                        messageChatMemoryAdvisor,
                        questionAnswerAdvisor,
                        simpleLoggerAdvisor
                        )
                .defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients))
                .build();
    }

    @Bean
    public SimpleLoggerAdvisor simpleLoggerAdvisor() {
        return new SimpleLoggerAdvisor();
    }

    @Bean
    public MessageChatMemoryAdvisor messageChatMemoryAdvisor(JdbcChatMemoryRepository chatMemoryRepository) {

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .build();

        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }

    @Bean
    public QuestionAnswerAdvisor questionAnswerAdvisor(VectorStore vectorStore) {
        QuestionAnswerAdvisor questionAnswerAdvisor = new QuestionAnswerAdvisor(vectorStore);
        return questionAnswerAdvisor;
    }

}
