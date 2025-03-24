package com.grupoeimsa.sigeim.models.licenses.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestLicensesDTO {
    private String search;
    private String departament;
    private String enterprise;
    private int page;
    private int size;
    private Boolean status;
}
