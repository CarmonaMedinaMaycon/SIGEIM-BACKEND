package com.grupoeimsa.sigeim.models.invoices.service;

import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.InvoiceDto;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.RequestGetAllInvoicesDto;
import com.grupoeimsa.sigeim.models.invoices.model.BeanInvoice;
import com.grupoeimsa.sigeim.models.invoices.model.IInvoice;
import com.grupoeimsa.sigeim.utils.CustomException;
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
    private final IComputerEquipament computingEquipmentRepository;
    public InvoiceService(IInvoice repository , IComputerEquipament computingEquipmentRepository) {
        this.computingEquipmentRepository = computingEquipmentRepository;
        this.repository = repository;
    }

    public BeanInvoice saveInvoice(InvoiceDto invoiceDTO) throws IOException {
        BeanInvoice invoice = new BeanInvoice();
        invoice.setTotal_iva(invoiceDTO.getTotal_iva());
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
                        invoice.getInvoiceId(),
                        invoice.getInvoiceFolio(),
                        invoice.getSupplier(),
                        invoice.getInvoiceDate(),
                        invoice.getTotal_iva(),
                        invoice.getComputerEquipament() != null ? invoice.getComputerEquipament().size() : 0
                ));
    }

    public void deleteInvoice(Long id) {
        BeanInvoice invoice = repository.findById(id)
                .orElseThrow(() -> new CustomException("Factura no encontrada con ID: " + id));

        int relatedEquipments = computingEquipmentRepository.countByInvoice_InvoiceId(id);
        if (relatedEquipments > 0) {
            throw new CustomException("No se puede eliminar la factura porque está asociada a uno o más equipos.");
        }

        repository.delete(invoice);
    }


    public ResponseEntity<byte[]> downloadInvoice(Long invoiceId) {
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
