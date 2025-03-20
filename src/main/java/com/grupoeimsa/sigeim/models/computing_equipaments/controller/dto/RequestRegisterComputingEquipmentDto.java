package com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestRegisterComputingEquipmentDto {
    final String serialNumber;
    final String idEsset;
    final Long personId;
    final String departament;
    final String enterprise;
    final String workModality;
    final String type;
    final String brand;
    final String model;
    final Long ramMemoryCapacity;
    final Long memoryCapacity;
    final String processor;
    final String purchasingCompany;
    final Boolean hasInvoice;
    final String supplier;
    final String invoiceFolio;
    final String purchaseDate;
    final String assetNumber;
    final Double price;
    final String systemObservations;
}
