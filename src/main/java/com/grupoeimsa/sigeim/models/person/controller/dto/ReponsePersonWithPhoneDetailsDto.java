package com.grupoeimsa.sigeim.models.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReponsePersonWithPhoneDetailsDto {
    private Long personId;
    private String fullName;
    private String departament;
    private String enterprise;
    private Boolean hasLicence;
    private Boolean hasCellphone;
}
