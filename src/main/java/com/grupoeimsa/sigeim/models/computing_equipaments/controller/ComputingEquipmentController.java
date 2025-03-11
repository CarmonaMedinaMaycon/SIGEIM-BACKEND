package com.grupoeimsa.sigeim.models.computing_equipaments.controller;

import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestAssingToSistemasDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestChangeStatusDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestEquipmentDetailsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestEquipmentsPaginationDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestRegisterComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestSearchByFilteringEquipmentsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestUpdateComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.ResponseSeeAllEquipmentsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.ResponseSeeDetailsEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.service.ComputingEquipmentService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/sigeim/computing-equipment")
public class ComputingEquipmentController {
    private final ComputingEquipmentService computingEquipmentService;
    public ComputingEquipmentController(ComputingEquipmentService computingEquipmentService) {
        this.computingEquipmentService = computingEquipmentService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerComputingEquipment(@RequestBody RequestRegisterComputingEquipmentDto dto) {
        String response = computingEquipmentService.createComputingEquipment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateComputingEquipment(@RequestBody RequestUpdateComputingEquipmentDto dto) {
        String response = computingEquipmentService.updateComputingEquipment(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/equipments")
    public Page<ResponseSeeAllEquipmentsDto> getEquipments(@RequestBody RequestEquipmentsPaginationDto paginationDto) {
        int page = paginationDto.getPage();
        int size = paginationDto.getSize();
        return computingEquipmentService.getAllEquipments(page, size);
    }

    @PostMapping("/search-equipment")
    public List<ResponseSeeAllEquipmentsDto> buscarEquipos(@RequestBody ResponseSeeAllEquipmentsDto searchCriteria) {
        return computingEquipmentService.searchEquipments(searchCriteria);
    }

    @PostMapping("/filters")
    public Map<String, List<String>> obtenerFiltrosDisponibles() {
        return computingEquipmentService.getAvailableFilters();
    }

    @PostMapping("/search-equipment-by-filtering")
    public Page<ResponseSeeAllEquipmentsDto> buscarEquipos(@RequestBody RequestSearchByFilteringEquipmentsDto filtros) {
        return computingEquipmentService.searchEquipments(filtros);
    }

    @PostMapping("/see-details")
    public ResponseSeeDetailsEquipmentDto verDetalles(@RequestBody RequestEquipmentDetailsDto requestDetails) {
        return computingEquipmentService.getEquipamentDetails(requestDetails.getId());
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

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = today.format(formatter);

        headers.add("Content-Disposition", "attachment; filename=SIGEIM-Bitacora-de-Equipos-de-Computo-" +
                formattedDate +
                ".xlsx");
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


}
