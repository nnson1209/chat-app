package com.learn.chat_app.controller;

import com.learn.chat_app.dto.request.LoginRequest;
import com.learn.chat_app.dto.response.ApiResponse;
import com.learn.chat_app.dto.response.LoginResponse;
import com.learn.chat_app.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        var data = authenticationService.login(request);

        return ApiResponse.<LoginResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Login successfully")
                .data(data)
                .build();
    }
}
