package com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEquipmentSelectDto {
    private Long equipmentId;
    private String serialNumber;
    private String type;
    private String brand;
    private String model;
    private String assetNumber;
}
