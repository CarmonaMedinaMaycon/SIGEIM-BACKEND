package com.grupoeimsa.sigeim.models.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDisablePersonDTO {
    private Long id;
    private String name;
    private String surname;
    private String lastname;
    private String email;
    private String reasonForExit;
    private String terminationComments;
    private LocalDate exitDate;
    private String terminationHandledBy;
    private String emailTerminationHandledBy;
    private Boolean deliveredEquipment;
    private Boolean deliveredPhone;
    private Boolean deliveredAccessCard;

}
