package com.grupoeimsa.sigeim.models.invoices.controller;

import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.AssignEquipmentToInvoiceRequest;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.InvoiceDetailsDto;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.InvoiceDto;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.RequestGetAllInvoicesDto;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.RequestSearchInvoiceDto;
import com.grupoeimsa.sigeim.models.invoices.model.BeanInvoice;
import com.grupoeimsa.sigeim.models.invoices.model.IInvoice;
import com.grupoeimsa.sigeim.models.invoices.service.InvoiceService;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/sigeim/invoice")
@CrossOrigin(origins = "*")
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final IInvoice repository;
    public InvoiceController(final InvoiceService invoiceService , final IInvoice repo) {
        this.repository = repo;
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

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteInvoice(@RequestBody Map<String, Long> body) {
        Long id = body.get("invoiceId");
        if (id == null) {
            throw new IllegalArgumentException("El ID de la factura es obligatorio.");
        }

        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok("Factura eliminada correctamente");
    }

    @GetMapping("/all")
    public ResponseEntity<Page<RequestGetAllInvoicesDto>> getAllInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String supplier,
            @RequestParam(required = false) String search
    ) {
        Page<RequestGetAllInvoicesDto> invoices = invoiceService.getAllInvoices(page, size, supplier, search);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable int id) {
        return invoiceService.downloadInvoice((long) id);
    }

    @GetMapping("/suppliers")
    public ResponseEntity<List<String>> getAllSuppliers() {
        List<String> suppliers = repository.findAll()
                .stream()
                .map(BeanInvoice::getSupplier)
                .distinct()
                .toList();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<InvoiceDetailsDto> getInvoiceDetails(@PathVariable Long id) {
        BeanInvoice invoice = repository.findById(id)
                .orElseThrow(() -> new CustomException("Factura no encontrada"));

        InvoiceDetailsDto dto = invoiceService.mapToDTO(invoice);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/assign-equipment")
    public ResponseEntity<?> assignEquipment(@RequestBody AssignEquipmentToInvoiceRequest request) {
        invoiceService.assignEquipmentToInvoice(request.getInvoiceId(), request.getComputerEquipamentId());
        return ResponseEntity.ok("Equipo asignado correctamente");
    }

    @GetMapping("/unassigned-equipments")
    public ResponseEntity<List<BeanComputerEquipament>> getEquipmentsWithoutInvoice() {
        return ResponseEntity.ok(invoiceService.getEquipmentsWithoutInvoice());
    }
}
