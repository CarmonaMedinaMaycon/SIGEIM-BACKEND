package com.grupoeimsa.sigeim.models.invoices.controller.dto;

import lombok.Data;

@Data
public class AssignEquipmentToInvoiceRequest {
    private Long invoiceId;
    private Long computerEquipamentId;
}