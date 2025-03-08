package com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSeeAllEquipmentsDto {
    private String serialNumber;
    private String idEsset;
    private String responsibleName;
    private String departament;
    private String type;
    private String brand;
    private String equipmentStatus;
}
