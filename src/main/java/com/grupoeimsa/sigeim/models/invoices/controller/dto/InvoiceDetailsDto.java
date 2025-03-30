package com.grupoeimsa.sigeim.models.invoices.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailsDto {
    private int invoiceId;
    private String invoiceFolio;
    private String supplier;
    private String invoiceDate;
    private Double totalIva;
    private List<EquipmentDTO> equipments;
    @Data
    public static class EquipmentDTO {
        private Long equipmentId;
        private String brand;
        private String serialNumber;
    }
}
