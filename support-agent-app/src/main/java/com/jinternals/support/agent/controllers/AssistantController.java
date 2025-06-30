package com.jinternals.support.agent.controllers;

import com.jinternals.support.agent.domain.Answer;
import com.jinternals.support.agent.domain.Question;
import com.jinternals.support.agent.services.AssistantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AssistantController {

    private final AssistantService assistantService;

    @PostMapping("/ai/assistant/{conversationId}")
    public Answer askQuestion(@PathVariable("conversationId") String conversationId,
                                    @RequestBody Question question) {
        return assistantService.getAnswer(conversationId, question);
    }


}
