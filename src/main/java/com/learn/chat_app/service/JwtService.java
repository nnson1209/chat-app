package com.learn.chat_app.service;

import com.learn.chat_app.exception.AppException;
import com.learn.chat_app.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.learn.chat_app.constant.AppConstant.AUTHORITIES;

@Service
@Slf4j(topic = "JWT-SERVICE")
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String generateAccessToken(String userId, Set<String> authorities) {
        JWSAlgorithm algorithm = JWSAlgorithm.HS512;
        JWSHeader header = new JWSHeader(algorithm);

        Date issueTime = new Date();
        Date expiredTime = new Date(Instant.now().plus(2, ChronoUnit.HOURS).toEpochMilli());

        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issueTime(issueTime)
                .expirationTime(expiredTime)
                .jwtID(jwtId)
                .claim(AUTHORITIES, authorities)
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_GENERATION_FAILED);
        }

        return jwsObject.serialize();
    }

    public String generateRefreshToken(String userId) {
        JWSAlgorithm algorithm = JWSAlgorithm.HS512;
        JWSHeader header = new JWSHeader(algorithm);

        Date issueTime = new Date();
        Date expiredTime = new Date(Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli());

        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issueTime(issueTime)
                .expirationTime(expiredTime)
                .jwtID(jwtId)
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_GENERATION_FAILED);
        }

        return jwsObject.serialize();
    }
}

