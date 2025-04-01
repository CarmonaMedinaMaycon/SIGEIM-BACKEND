package com.grupoeimsa.sigeim.models.computing_equipaments.controller;

import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestAssingToSistemasDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestChangeStatusDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestEquipmentDetailsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestEquipmentsPaginationDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestRegisterComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestSearchByFilteringEquipmentsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestUpdateComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.ResponseEditComputerEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.ResponseEquipmentSelectDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.ResponseSeeAllEquipmentsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.ResponseSeeDetailsEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.SearchEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.service.ComputingEquipmentService;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.InvoiceDto;
import com.grupoeimsa.sigeim.models.invoices.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/sigeim/computing-equipment")
@CrossOrigin(origins = {"*"})
public class ComputingEquipmentController {
    private final ComputingEquipmentService computingEquipmentService;
    private final InvoiceService invoiceService;

    public ComputingEquipmentController(ComputingEquipmentService computingEquipmentService
            , InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
        this.computingEquipmentService = computingEquipmentService;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerComputingEquipment(
            @ModelAttribute RequestRegisterComputingEquipmentDto dto
    ) {
        try {
            String response = computingEquipmentService.createComputingEquipment(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getComputingEquipment(@PathVariable Long id) {
        try {
            ResponseEditComputerEquipmentDto dto = computingEquipmentService.getComputingEquipment(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al obtener la información");
        }
    }


    @PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> editEquipment(
            @ModelAttribute RequestRegisterComputingEquipmentDto dto
    ) {
        try {
            String message = computingEquipmentService.editComputingEquipment(dto);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al editar el equipo: " + e.getMessage());
        }
    }

    @PostMapping("/search-equipment")
    public List<ResponseSeeAllEquipmentsDto> buscarEquipos(@RequestBody SearchEquipmentDto searchCriteria) {
        return computingEquipmentService.searchEquipments(searchCriteria.getSearchQuery());
    }

    @PostMapping("/filters")
    public Map<String, List<String>> obtenerFiltrosDisponibles() {
        return computingEquipmentService.getAvailableFilters();
    }

    @PostMapping("/search-equipment-by-filtering")
    public Page<ResponseSeeAllEquipmentsDto> buscarEquipos(@RequestBody RequestSearchByFilteringEquipmentsDto filtros) {
        return computingEquipmentService.searchEquipmentsFiltering(filtros);
    }


    @PostMapping("/change-status")
    public ResponseEntity<String> changeStatus(@RequestBody RequestChangeStatusDto request) {
        String response = computingEquipmentService.changeStatus(request.getEquipmentId(), request.getNewStatus());
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PutMapping("/assign-sistemas")
    public ResponseEntity<BeanComputerEquipament> assignToSistemas(@RequestBody @Valid RequestAssingToSistemasDto request) {
        BeanComputerEquipament updatedEquipament = computingEquipmentService.assignToSistemas(request.getEquipmentId(), request.getSistemasPersonId());
        return ResponseEntity.ok(updatedEquipament);
    }

    @GetMapping("/export-to-excel")
    public ResponseEntity<InputStreamResource> exportToExcel() throws IOException {

        byte[] excelData = computingEquipmentService.generateExcelFile();

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(excelData));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @PostMapping("/generate-qr")
    public ResponseEntity<byte[]> generateEquipmentQr(@RequestBody RequestEquipmentDetailsDto request) throws Exception {
        String qrContent = "api/sigeim/computing-equipments/mobile-details/" + request.getId();

        byte[] qrImage = computingEquipmentService.generateQRCodeImage(qrContent, 500, 500);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("qr_equipment_" + request.getId() + ".png")
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(qrImage);
    }


    @GetMapping("/mobile-details/{id}")
    public ResponseEntity<ResponseSeeDetailsEquipmentDto> getEquipmentDetails(@PathVariable Long id) {
        ResponseSeeDetailsEquipmentDto equipmentDetails = computingEquipmentService.getEquipamentDetails(id);
        return ResponseEntity.ok(equipmentDetails);
    }


    @PostMapping("/select-responsive-equipment-list")
    public List<ResponseEquipmentSelectDto> getEquipmentsForResponsive() {
        return computingEquipmentService.getAllEquipmentsForResponsiveGeneration();
    }

}
