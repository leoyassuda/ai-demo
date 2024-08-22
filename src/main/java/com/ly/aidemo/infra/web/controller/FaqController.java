package com.ly.aidemo.infra.web.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import com.ly.aidemo.app.usecases.FaqAnswerUseCase;
import com.ly.aidemo.infra.web.models.LLMRequest;
import com.ly.aidemo.infra.web.models.LLResponse;

@RestController
public class FaqController {
    private FaqAnswerUseCase aiService;

    public FaqController(FaqAnswerUseCase aiService) {
        this.aiService = aiService;
    }

    @PostMapping("api/v1/generate")
    public ResponseEntity<LLResponse> generate(@RequestBody LLMRequest request) {
        String chatResponse = aiService.generate(request.getQuery());
        LLResponse response = new LLResponse("Success", chatResponse);
        return ResponseEntity.ok(response);
    }

}
