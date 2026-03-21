package com.learn.chat_app.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String userId;
    private String accessToken;
    private String refreshToken;
}

