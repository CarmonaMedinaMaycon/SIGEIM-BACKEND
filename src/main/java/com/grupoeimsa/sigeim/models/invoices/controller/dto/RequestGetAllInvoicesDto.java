package com.grupoeimsa.sigeim.models.invoices.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestGetAllInvoicesDto {
    private int invoiceId;
    private String invoiceFolio;
    private String supplier;
    private String invoiceDate;
    private double totalIva;
    private int equipmentCount;
}
