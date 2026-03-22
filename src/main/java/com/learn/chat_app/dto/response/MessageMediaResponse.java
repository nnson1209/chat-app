package com.learn.chat_app.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageMediaResponse(
        String fileName,     // Tên file
        String fileType,     // Loại file
        String thumbnailUrl, // URL của file
        LocalDateTime uploadedAt // Thời gian upload
) {
}

