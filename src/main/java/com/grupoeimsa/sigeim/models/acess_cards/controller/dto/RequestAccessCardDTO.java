package com.grupoeimsa.sigeim.models.acess_cards.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestAccessCardDTO {
    private String search;
    private int page;
    private int size;
}