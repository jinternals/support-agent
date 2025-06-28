package com.jinternals.support.agent.controllers;

import com.jinternals.support.agent.domain.Answer;
import com.jinternals.support.agent.domain.Question;
import com.jinternals.support.agent.services.AssistantService;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@AllArgsConstructor
public class AssistantController {

    private final AssistantService assistantService;

    @PostMapping("/ai/assistant/{conversationId}")
    public Flux<String> askQuestion(@PathVariable("conversationId") String conversationId,
                                    @RequestBody Question question) {
        return assistantService.getAnswer(conversationId, question);
    }


}
