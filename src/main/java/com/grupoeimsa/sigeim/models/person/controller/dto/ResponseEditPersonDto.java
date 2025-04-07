package com.grupoeimsa.sigeim.models.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEditPersonDto {
    private Long id;
    private String name;
    private String surname;
    private String lastname;
    private String whoRegistered;
    private String emailRegistered;
    private String email;
    private String phoneNumber;
    private String phoneNumberAssigned;
    private String departament;
    private String enterprise;
    private String position;
    private String comments;
    private String commentsHardwareSoftware;
    private String commentsEmail;
    private LocalDate entryDate;
}
