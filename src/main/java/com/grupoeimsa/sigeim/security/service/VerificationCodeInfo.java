package com.grupoeimsa.sigeim.security.service;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VerificationCodeInfo {
    private final String code;
    private final LocalDateTime expirationTime;

    public VerificationCodeInfo(String code, LocalDateTime expirationTime) {
        this.code = code;
        this.expirationTime = expirationTime;
    }

}
