package com.learn.chat_app.service;

import com.learn.chat_app.dto.request.LoginRequest;
import com.learn.chat_app.dto.response.LoginResponse;
import com.learn.chat_app.entity.User;
import com.learn.chat_app.exception.AppException;
import com.learn.chat_app.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        User user = (User) authentication.getPrincipal();

        if(user == null)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Set<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String accessToken = jwtService.generateAccessToken(user.getId(), authorities);
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return LoginResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

