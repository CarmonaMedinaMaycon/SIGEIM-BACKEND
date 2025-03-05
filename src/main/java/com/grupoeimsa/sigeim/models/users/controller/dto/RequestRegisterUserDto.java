package com.grupoeimsa.sigeim.models.users.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestRegisterUserDto {

    // Datos de la Persona
    private String name;
    private String surname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String departament;
    private Integer role;
    private String enterprise;
    private String position;
    private String password;
}
