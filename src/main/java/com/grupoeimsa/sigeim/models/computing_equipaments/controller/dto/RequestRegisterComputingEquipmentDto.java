package com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class RequestRegisterComputingEquipmentDto {
    final String serialNumber;
    final String idEsset;
    final Long personId;
    final String departament;
    final String enterprise;
    final String place;
    final String type;
    final String brand;
    final String model;
    final Long ramMemoryCapacity;
    final Long memoryCapacity;
    final String processor;
    final String purchasingCompany;
    final Boolean hasInvoice;
    final String supplier;
    final Long invoiceFolio;
    final Date purchaseDate;
    final String assetNumber;
    final Long price;
    final Integer status;
    final String systemObservations;
}
