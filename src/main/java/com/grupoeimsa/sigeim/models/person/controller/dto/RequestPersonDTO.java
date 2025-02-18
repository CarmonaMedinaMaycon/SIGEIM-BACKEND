package com.grupoeimsa.sigeim.models.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestPersonDTO {
    private String search;
    private int page;
    private int size;
}
