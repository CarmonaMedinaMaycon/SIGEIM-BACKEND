package com.grupoeimsa.sigeim.security.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestChangePasswordDto {
    private String email;
    private String verificationCode;
    private String newPassword;
    private String repeatNewPassword;
}
