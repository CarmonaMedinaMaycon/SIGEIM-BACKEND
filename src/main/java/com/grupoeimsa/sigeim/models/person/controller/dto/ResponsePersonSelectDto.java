package com.grupoeimsa.sigeim.models.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePersonSelectDto {
    private Long personId;
    private String fullName;
    private String personDepartament;
    private String personPosition;
}
