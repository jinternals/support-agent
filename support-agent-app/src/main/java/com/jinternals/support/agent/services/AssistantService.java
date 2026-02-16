package com.jinternals.support.agent.services;

import com.jinternals.support.agent.domain.Answer;
import com.jinternals.support.agent.domain.Question;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor.FILTER_EXPRESSION;
import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@AllArgsConstructor
@Slf4j
public class AssistantService {

    private final ChatClient chatClient;
    @Value("classpath:/prompts/user-prompt.txt")
    private Resource userPromptResource;

    public Answer getAnswer(String conversationId, Question question) {

        PromptTemplate promptTemplate = new PromptTemplate(userPromptResource);
        promptTemplate.add("userId", question.userId());
        promptTemplate.add("question", question.question());
        Prompt prompt = new  Prompt(promptTemplate.createMessage());

        String result = this.chatClient.prompt(prompt)
               // .system(sp -> sp.param("userId",  question.userId()))
                .advisors(a -> a
                        .param("userId",  question.userId())
                        .param(CONVERSATION_ID, conversationId)
                        .param(FILTER_EXPRESSION, "context == 'support'")
                )
                .call().content();

        return new Answer(result);
    }

}
