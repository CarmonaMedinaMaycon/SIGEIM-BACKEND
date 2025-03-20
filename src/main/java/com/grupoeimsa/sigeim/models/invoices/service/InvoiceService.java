package com.grupoeimsa.sigeim.models.invoices.service;

import com.grupoeimsa.sigeim.models.invoices.controller.dto.InvoiceDto;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.RequestGetAllInvoicesDto;
import com.grupoeimsa.sigeim.models.invoices.model.BeanInvoice;
import com.grupoeimsa.sigeim.models.invoices.model.IInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class InvoiceService {
    private final IInvoice repository;
    public InvoiceService(IInvoice repository) {
        this.repository = repository;
    }

    public BeanInvoice saveInvoice(InvoiceDto invoiceDTO) throws IOException {
        BeanInvoice invoice = new BeanInvoice();
        invoice.setPriceIva(invoiceDTO.getPriceIva());
        invoice.setSupplier(invoiceDTO.getSupplier());
        invoice.setInvoiceDate(invoiceDTO.getInvoiceDate());
        invoice.setInvoiceFolio(invoiceDTO.getInvoiceFolio());
        invoice.setInvoiceFile(invoiceDTO.getFile().getBytes());

        return repository.save(invoice);
    }

    public Optional<BeanInvoice> findByInvoiceFolio(String invoiceFolio) {
        return repository.findByInvoiceFolio(invoiceFolio);
    }

    public Page<RequestGetAllInvoicesDto> getAllInvoices(PageRequest pageRequest) {
        return repository.findAll(pageRequest)
                .map(invoice -> new RequestGetAllInvoicesDto(
                        invoice.getInvoice_id(),
                        invoice.getInvoiceFolio(),
                        invoice.getSupplier(),
                        invoice.getInvoiceDate(),
                        invoice.getPriceIva() // Aquí debe asignarse correctamente
                ));
    }

    public ResponseEntity<byte[]> downloadInvoice(int invoiceId) {
        Optional<BeanInvoice> invoiceOpt = repository.findById(invoiceId);

        if (invoiceOpt.isPresent()) {
            BeanInvoice invoice = invoiceOpt.get();
            byte[] fileContent = invoice.getInvoiceFile(); // Obtener archivo desde la base de datos

            if (fileContent == null || fileContent.length == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Factura_" + invoice.getInvoiceFolio() + ".pdf");
            headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
