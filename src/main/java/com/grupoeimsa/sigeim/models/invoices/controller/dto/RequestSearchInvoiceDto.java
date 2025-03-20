package com.grupoeimsa.sigeim.models.invoices.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestSearchInvoiceDto {
    private String supplier;
    private String invoiceFolio;
    private String invoiceDate;
    private Integer page;
    private Integer size;
}