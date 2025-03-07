package com.grupoeimsa.sigeim.models.cellphones.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestCellphoneDTO {
    private String search;
    private int page;
    private int size;
}
