package com.grupoeimsa.sigeim.models.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePersonalInfoDto {
    private Long personId;
    private String name;
    private String surname;
    private String lastname;
    private String enterprise;
    private String departament;
    private String position;
    private LocalDate entryDate;
    private String phoneNumber;
    private String email;
    private String executiveCode;
}
