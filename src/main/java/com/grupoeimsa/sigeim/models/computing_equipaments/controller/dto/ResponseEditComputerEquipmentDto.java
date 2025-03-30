package com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto;

import com.grupoeimsa.sigeim.models.computing_equipaments.model.CEStatus;
import lombok.Data;

@Data
public class ResponseEditComputerEquipmentDto {
    private Long computerEquipamentId;

    // Datos del equipo
    private String serialNumber;
    private String idEsset;
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
    private String supplier;
    private Boolean hasInvoice;
    private String invoiceFolio;
    private String purchaseDate;
    private String assetNumber;
    private Double price;
    private String systemObservations;
    private CEStatus status;

    private Long personId;

    // Datos de la factura (si existe)
    private Double totalIva;
    private String invoiceDate;
    private String supplierInvoice;
    private String invoiceFolioInvoice;

    // URL para descargar o visualizar el archivo (si existe)
    private String invoiceFileBase64;

}