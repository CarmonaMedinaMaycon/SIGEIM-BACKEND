package com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestEquipmentsPaginationDto {
    private int page;
    private int size;
}
