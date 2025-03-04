package com.grupoeimsa.sigeim.security.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseAuthDto{
    String username;
    String token;
    String role;
    String logo;
    String color;
}