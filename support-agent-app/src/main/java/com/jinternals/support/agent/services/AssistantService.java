package com.jinternals.support.agent.services;

import com.jinternals.support.agent.domain.Answer;
import com.jinternals.support.agent.domain.Question;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor.FILTER_EXPRESSION;
import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@AllArgsConstructor
@Slf4j
public class AssistantService {
    private final ChatClient chatClient;

    public Flux<String> getAnswer(String conversationId,  Question question) {

        List<Message> messages = List.of(new UserMessage(question.question()));

        Prompt prompt = new Prompt(messages);

        return this.chatClient.prompt(prompt)
                .system(sp -> sp.param("userId",  question.userId()))
                .advisors(a -> a
                        .param(CONVERSATION_ID, conversationId)
                        .param(FILTER_EXPRESSION, "client == 'xyz'"))
                .stream().content();
    }

//    void loadPrompt(String name) {
//        McpSchema.GetPromptRequest r = new McpSchema.GetPromptRequest(name, Map.of());
//        var client = mcpSyncClients.stream()
//                .filter(c -> c.getServerInfo().name().equals("meeting-scheduler-mcp-server"))
//                .findFirst();
//        if (client.isPresent()) {
//            var content = (McpSchema.TextContent) client.get()
//                    .getPrompt(r)
//                    .messages()
//                    .getFirst()
//                    .content();
//            log.info("Prompt: {}", content.text());
//        }
//    }
}
