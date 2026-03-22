package com.learn.chat_app.dto.request;

public record MessageMediaRequest(
        String fileName,     // Tên file (ví dụ: "image.jpg")
        String fileType,     // Loại file (ví dụ: "image/jpeg")
        String thumbnailUrl  // URL của file đã upload (S3, Cloudinary, etc.)
) {
}

