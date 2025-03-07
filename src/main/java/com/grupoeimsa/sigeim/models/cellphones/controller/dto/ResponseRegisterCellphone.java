package com.grupoeimsa.sigeim.models.cellphones.controller.dto;

import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseRegisterCellphone {
    private Long cellphoneId;

    private String legalName;

    private String company;

    private int shortDialing; //marcacion rapida

    private LocalDate dateRenovation;

    private String imei;

    private String comments;

    private Boolean status;

    private BeanPerson person;
}
