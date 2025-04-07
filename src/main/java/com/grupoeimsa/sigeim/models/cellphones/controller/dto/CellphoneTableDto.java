package com.grupoeimsa.sigeim.models.cellphones.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellphoneTableDto {
    private Long cellphoneId;
    private String equipamentName;
    private String company;
    private String imei;
    private int shortDialing;
    private String legalName;
    private String assignedTo;
    private LocalDate dateRenovation;
    private String comments;
    private String userFullName;
    private Boolean status;
}
