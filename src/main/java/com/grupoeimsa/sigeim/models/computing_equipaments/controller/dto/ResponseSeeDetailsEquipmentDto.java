package com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto;

import com.grupoeimsa.sigeim.models.history_photos.model.controller.dto.HistoryEquipmentPhotosGroupDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ResponseSeeDetailsEquipmentDto {
    private String serialNumber;
    private String idEsset;
    private String responsible;
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
    private Boolean hasInvoice;
    private String supplier;
    private Long invoiceFolio;
    private LocalDate purchaseDate;
    private String assetNumber;
    private Double price;
    private String status;
    private String systemObservations;
    private LocalDate creationDate;
    private LocalDate lastUpdateDate;
}
