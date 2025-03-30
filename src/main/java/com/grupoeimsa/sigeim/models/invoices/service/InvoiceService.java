package com.grupoeimsa.sigeim.models.invoices.service;

import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.InvoiceDetailsDto;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.InvoiceDto;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.RequestGetAllInvoicesDto;
import com.grupoeimsa.sigeim.models.invoices.model.BeanInvoice;
import com.grupoeimsa.sigeim.models.invoices.model.IInvoice;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
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

    public Page<RequestGetAllInvoicesDto> getAllInvoices(int page, int size, String supplier, String search) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<BeanInvoice> spec = Specification.where(null);

        System.out.println("SUPPLIER: " + supplier);
        System.out.println("SEARCH: " + search);

        if (supplier != null && !supplier.equalsIgnoreCase("Todos")) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("supplier"), supplier));
        }

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("invoiceFolio")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("supplier")), "%" + search.toLowerCase() + "%")
            ));
        }

        return repository.findAll(spec, pageable).map(invoice -> new RequestGetAllInvoicesDto(
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

    public InvoiceDetailsDto mapToDTO(BeanInvoice invoice) {
        InvoiceDetailsDto dto = new InvoiceDetailsDto();
        dto.setInvoiceId(invoice.getInvoiceId());
        dto.setInvoiceFolio(invoice.getInvoiceFolio());
        dto.setSupplier(invoice.getSupplier());
        dto.setInvoiceDate(invoice.getInvoiceDate());
        dto.setTotalIva(invoice.getTotal_iva());

        List<InvoiceDetailsDto.EquipmentDTO> equipmentDTOs = invoice.getComputerEquipament()
                .stream()
                .map(e -> {
                    InvoiceDetailsDto.EquipmentDTO eqDTO = new InvoiceDetailsDto.EquipmentDTO();
                    eqDTO.setBrand(e.getBrand());
                    eqDTO.setSerialNumber(e.getSerialNumber());
                    eqDTO.setEquipmentId(e.getComputerEquipamentId());
                    return eqDTO;
                })
                .toList();

        dto.setEquipments(equipmentDTOs);
        return dto;
    }

    public void assignEquipmentToInvoice(Long invoiceId, Long equipmentId) {
        BeanInvoice invoice = repository.findById(invoiceId)
                .orElseThrow(() -> new CustomException("Factura no encontrada"));

        BeanComputerEquipament equip = computingEquipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new CustomException("Equipo no encontrado"));

        equip.setHasInvoice(true);
        equip.setInvoice(invoice); // Asignamos la relación
        computingEquipmentRepository.save(equip);
    }

    public List<BeanComputerEquipament> getEquipmentsWithoutInvoice() {
        return computingEquipmentRepository.findByInvoiceIsNull();
    }

}
