package com.grupoeimsa.sigeim.models.cellphones.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseToGenerateResponsiveDto {
    private Long cellphoneId;
    private String imei;
    private String number;
}
