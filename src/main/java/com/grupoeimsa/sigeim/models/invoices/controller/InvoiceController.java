package com.grupoeimsa.sigeim.models.invoices.controller;

import com.grupoeimsa.sigeim.models.invoices.controller.dto.InvoiceDto;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.RequestGetAllInvoicesDto;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.RequestSearchInvoiceDto;
import com.grupoeimsa.sigeim.models.invoices.model.BeanInvoice;
import com.grupoeimsa.sigeim.models.invoices.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.io.IOException;

@RestController
@RequestMapping("api/sigeim/invoice")
@CrossOrigin(origins = "*")
public class InvoiceController {
    private final InvoiceService invoiceService;
    public InvoiceController(final InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerInvoice(@ModelAttribute InvoiceDto invoiceDTO) {
        try {
            BeanInvoice invoice = invoiceService.saveInvoice(invoiceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el archivo.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Page<RequestGetAllInvoicesDto>> getAllInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RequestGetAllInvoicesDto> invoices = invoiceService.getAllInvoices(PageRequest.of(page, size));
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable int id) {
        return invoiceService.downloadInvoice(id);
    }
}
