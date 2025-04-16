package com.grupoeimsa.sigeim.models.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCellphoneDto {
    private String imei;
    private String company;
    private String shortDialing;
    private String dateRenovation;
    private Long cellphoneId;
}
