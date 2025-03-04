package com.grupoeimsa.sigeim.security.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record RequestAuthDto(@NotBlank(message = "User or password incorrect") String username,
                             @NotBlank(message = "User or password incorrect") String password) {
}
