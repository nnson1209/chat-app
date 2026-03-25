package com.learn.chat_app.dto.request;

import com.learn.chat_app.common.ConversationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateConversationRequest(
        String name,
        String conversationAvatar,

        @NotNull(message = "Conversation type is required")
        ConversationType conversationType,

        @NotEmpty(message = "Participant ids are required")
        List<String> participantIds
) {}
