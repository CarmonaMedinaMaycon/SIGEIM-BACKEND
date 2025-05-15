package com.grupoeimsa.sigeim.models.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLicencesPersonSelectDto {
    private Long personId;
    private String personName;
    private String departament;
    private String phoneNumber;
}
