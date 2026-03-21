package com.learn.chat_app.mapper;

import com.learn.chat_app.common.ConversationType;
import com.learn.chat_app.dto.response.CreateConversationResponse;
import com.learn.chat_app.dto.response.ParticipantResponse;
import com.learn.chat_app.entity.Conversation;

public final class ConversationMapper {
    private ConversationMapper() {
    }

    public static CreateConversationResponse toConversationResponse(String creatorId, Conversation conversation) {
        ConversationType conversationType = conversation.getConversationType();

        // Build response với các thông tin cơ bản
        CreateConversationResponse response = CreateConversationResponse.builder()
                .id(conversation.getId())
                .conversationType(conversationType)
                // Map danh sách participants sang ParticipantResponse
                .participantInfo(conversation.getParticipants().stream()
                        .map(participants -> ParticipantResponse.builder()
                                .userId(participants.getUser().getId())
                                .username(participants.getUser().getUsername())
                                .build())
                        .toList())
                .createdAt(conversation.getCreatedAt())
                .build();

        // Xử lý tên conversation khác nhau cho PRIVATE và GROUP
        if (conversationType == ConversationType.PRIVATE) {
            // Với PRIVATE: tên là username của người còn lại (không phải creator)
            conversation.getParticipants()
                    .stream()
                    .filter(participants -> !participants.getUser().getId().equals(creatorId))
                    .findFirst()
                    .ifPresent(participantInfo -> response.setName(participantInfo.getUser().getUsername()));
        } else {
            // Với GROUP: dùng tên nhóm và avatar từ conversation
            response.setName(conversation.getName());
            response.setConversationAvatar(conversation.getConversationAvatar());
        }

        return response;
    }
}
