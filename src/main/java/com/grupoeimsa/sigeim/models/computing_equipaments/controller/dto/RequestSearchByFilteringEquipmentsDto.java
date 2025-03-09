package com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestSearchByFilteringEquipmentsDto {
    private String type;
    private String supplier;
    private String equipmentStatus;
    private String brand;
    private Integer page;
    private Integer size;
}
