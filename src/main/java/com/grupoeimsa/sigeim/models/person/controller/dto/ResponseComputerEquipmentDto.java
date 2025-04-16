package com.grupoeimsa.sigeim.models.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseComputerEquipmentDto {
    private Long computerEquipamentId;
    private String serialNumber;
    private String idEsset;
    private String brand;
    private String model;
    private String type;
    private String status;
    private String assetNumber;
    private boolean hasSignedOrPendingResponsive;
    private Long responsiveId;
}