package com.grupoeimsa.sigeim.models.invoices.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {
    private Double total_iva;
    private String supplier;
    private String invoiceDate;
    private String invoiceFolio;
    private MultipartFile file;
}
