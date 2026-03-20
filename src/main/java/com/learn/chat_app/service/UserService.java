package com.learn.chat_app.service;

import com.learn.chat_app.dto.request.CreateUserRequest;
import com.learn.chat_app.dto.response.CreateUserResponse;
import com.learn.chat_app.entity.Role;
import com.learn.chat_app.entity.User;
import com.learn.chat_app.exception.AppException;
import com.learn.chat_app.exception.ErrorCode;
import com.learn.chat_app.repository.RoleRepository;
import com.learn.chat_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.learn.chat_app.constant.AppConstant.USER_ROLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public CreateUserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        Role role = roleRepository.findByName(USER_ROLE)
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name(USER_ROLE)
                        .build()));
        user.addRole(role);

        userRepository.save(user);

        return CreateUserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
