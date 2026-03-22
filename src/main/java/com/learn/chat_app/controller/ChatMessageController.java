package com.learn.chat_app.controller;

import com.learn.chat_app.dto.request.ChatMessageRequest;
import com.learn.chat_app.dto.response.ApiResponse;
import com.learn.chat_app.dto.response.ChatMessageResponse;
import com.learn.chat_app.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @PostMapping
    ApiResponse<ChatMessageResponse> sendChatMessage(
            @AuthenticationPrincipal Jwt jwt, // Lấy thông tin user từ JWT
            @RequestBody @Valid ChatMessageRequest request) {

        var senderId = jwt.getSubject(); // Lấy userId từ JWT
        var data = chatMessageService.sendChatMessage(senderId, request);

        return ApiResponse.<ChatMessageResponse>builder()
                .code(HttpStatus.CREATED.value()) // 201 Created
                .message("Chat message sent successfully")
                .data(data)
                .build();
    }
}

