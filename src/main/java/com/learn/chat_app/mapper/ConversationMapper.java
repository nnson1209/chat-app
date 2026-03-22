package com.learn.chat_app.mapper;

import com.learn.chat_app.common.ConversationType;
import com.learn.chat_app.dto.response.ConversationDetailResponse;
import com.learn.chat_app.dto.response.CreateConversationResponse;
import com.learn.chat_app.dto.response.ParticipantResponse;
import com.learn.chat_app.entity.Conversation;

public final class ConversationMapper {
    private ConversationMapper() {
    }

    // Map cho Create Conversation response
    public static CreateConversationResponse toConversationResponse(String creatorId, Conversation conversation) {
        ConversationType conversationType = conversation.getConversationType();

        CreateConversationResponse response = CreateConversationResponse.builder()
                .id(conversation.getId())
                .conversationType(conversationType)
                .participantInfo(conversation.getParticipants().stream()
                        .map(participants -> ParticipantResponse.builder()
                                .userId(participants.getUser().getId())
                                .username(participants.getUser().getUsername())
                                .build())
                        .toList())
                .createdAt(conversation.getCreatedAt())
                .build();

        // Resolve tên conversation
        String name = resolveConversationName(creatorId, conversation);
        response.setName(name);

        // Chỉ set avatar cho GROUP conversation
        if (conversation.getConversationType() != ConversationType.PRIVATE) {
            response.setConversationAvatar(conversation.getConversationAvatar());
        }

        return response;
    }

    // Map cho Get My Conversations response
    public static ConversationDetailResponse toConversationDetailResponse(String creatorId, Conversation conversation) {
        ConversationType conversationType = conversation.getConversationType();

        ConversationDetailResponse response = ConversationDetailResponse.builder()
                .id(conversation.getId())
                .conversationType(conversationType)
                // Map danh sách participants
                .participantInfo(conversation.getParticipants().stream()
                        .map(participants -> ParticipantResponse.builder()
                                .userId(participants.getUser().getId())
                                .username(participants.getUser().getUsername())
                                .build())
                        .toList())
                // Thông tin tin nhắn cuối cùng
                .lastMessageId(conversation.getLastMessageId())
                .lastMessageContent(conversation.getLastMessageContent())
                .lastMessageTime(conversation.getLastMessageTime())
                .createdAt(conversation.getCreatedAt())
                .build();

        // Resolve tên conversation
        String name = resolveConversationName(creatorId, conversation);
        response.setName(name);

        // Chỉ set avatar cho GROUP conversation
        if (conversation.getConversationType() != ConversationType.PRIVATE) {
            response.setConversationAvatar(conversation.getConversationAvatar());
        }

        return response;
    }

    // Helper method để resolve tên conversation
    // PRIVATE: Tên của người còn lại (không phải creatorId)
    // GROUP: Tên nhóm
    private static String resolveConversationName(String creatorId, Conversation conversation) {
        if (conversation.getConversationType() == ConversationType.PRIVATE) {
            return conversation.getParticipants()
                    .stream()
                    .filter(p -> !p.getUser().getId().equals(creatorId)) // Lọc người còn lại
                    .findFirst()
                    .map(p -> p.getUser().getUsername()) // Lấy username
                    .orElse(null);
        }
        return conversation.getName(); // Trả về tên nhóm
    }
}
