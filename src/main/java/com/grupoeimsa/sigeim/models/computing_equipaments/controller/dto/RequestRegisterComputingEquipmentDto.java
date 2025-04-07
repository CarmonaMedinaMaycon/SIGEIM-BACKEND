package com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
public class RequestRegisterComputingEquipmentDto {
    @Nullable
    private Long computerEquipamentId;

    private final String serialNumber;
    private final String idEsset;
    private final Long personId;
    private final String departament;
    private final String enterprise;
    private final String workModality;
    private final String type;
    private final String brand;
    private final String model;
    private final Long ramMemoryCapacity;
    private final Long memoryCapacity;
    private final String processor;
    private final String purchasingCompany;
    private final Boolean hasInvoice;
    private final String supplier;
    private final String invoiceFolio;
    private final String purchaseDate;
    private final String assetNumber;
    private final Double price;
    private final String systemObservations;

    @Nullable
    private final Double totalIva;
    @Nullable
    private final MultipartFile file;
    @Nullable
    private final String supplierInvoice;
    @Nullable
    private final String invoiceDate;
}
