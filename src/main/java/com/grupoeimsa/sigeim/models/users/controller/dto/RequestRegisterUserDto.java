package com.grupoeimsa.sigeim.models.users.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestRegisterUserDto {
    private String email;
    private Integer role;
    private String password;
}
