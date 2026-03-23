package com.learn.chat_app.controller;

import com.learn.chat_app.dto.request.CreateUserRequest;
import com.learn.chat_app.dto.response.ApiResponse;
import com.learn.chat_app.dto.response.CreateUserResponse;
import com.learn.chat_app.dto.response.UserDetailResponse;
import com.learn.chat_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        var data = userService.createUser(request);

        return ApiResponse.<CreateUserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("User created successfully")
                .data(data)
                .build();
    }

    @GetMapping
    public ApiResponse<UserDetailResponse> myInfo(@AuthenticationPrincipal Jwt jwt) {
        // Extract userId từ JWT token subject
        var userId = jwt.getSubject();

        // Gọi service để lấy user info
        var data = userService.myInfo(userId);

        return ApiResponse.<UserDetailResponse>builder()
                .code(HttpStatus.OK.value())
                .message("User info retrieved successfully")
                .data(data)
                .build();
    }

}

