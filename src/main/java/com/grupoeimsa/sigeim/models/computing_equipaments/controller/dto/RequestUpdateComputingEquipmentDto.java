package com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateComputingEquipmentDto {
    private Long id;
    private String serialNumber;
    private String idEsset;
    private Long personId;
    private String departament;
    private String enterprise;
    private String workModality;
    private String type;
    private String brand;
    private String model;
    private Long ramMemoryCapacity;
    private Long memoryCapacity;
    private String processor;
    private String purchasingCompany;
    private Boolean updateInvoice;
    private String supplier;
    private String invoiceFolio;
    private String purchaseDate;
    private String assetNumber;
    private Double price;
    private String systemObservations;
}
