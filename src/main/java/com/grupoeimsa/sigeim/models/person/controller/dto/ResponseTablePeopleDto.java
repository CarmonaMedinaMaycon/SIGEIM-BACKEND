package com.grupoeimsa.sigeim.models.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTablePeopleDto {
    private Long personId;
    private String fullname;
    private String enterprise;
    private String departament;
    private String phoneNumber;
    private Boolean status;
    private List<String> equipamentsSerialNumber;
}
