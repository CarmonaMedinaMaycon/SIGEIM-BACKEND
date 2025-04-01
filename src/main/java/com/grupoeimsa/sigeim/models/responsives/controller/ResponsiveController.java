package com.grupoeimsa.sigeim.models.responsives.controller;

import com.grupoeimsa.sigeim.models.responsives.controller.dto.DownloadResponsiveDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.GenerateResponsiveDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.RequestSearchResponsiveEquipmentsDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseEditResponsiveEquipmentDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseResponsiveEquipmentsDto;
import com.grupoeimsa.sigeim.models.responsives.service.ResponsiveService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sigeim/responsives")
@CrossOrigin(origins = {"*"})
public class ResponsiveController {

    private final ResponsiveService responsiveService;

    public ResponsiveController(ResponsiveService responsiveService) {
        this.responsiveService = responsiveService;
    }

    @PostMapping("/generate-equipment-responsive")
    public ResponseEntity<String> generateResponsive(@RequestBody GenerateResponsiveDto dto) {
        try {
            responsiveService.generateResponsive(dto);
            return ResponseEntity.ok("Documento generado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al generar documento: " + e.getMessage());
        }
    }

    @PostMapping("/equipments")
    public ResponseEntity<Page<ResponseResponsiveEquipmentsDto>> getResponsivesEquipments(
            @RequestBody RequestSearchResponsiveEquipmentsDto dto) {
        Page<ResponseResponsiveEquipmentsDto> result = responsiveService.getResponsivesEquipments(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/equipments/download")
    public ResponseEntity<byte[]> downloadResponsive(@RequestBody DownloadResponsiveDto dto) {
        return responsiveService.downloadResponsive(dto);
    }

    @GetMapping("/get-edit-data/{id}")
    public ResponseEntity<ResponseEditResponsiveEquipmentDto> getEditResponsive(@PathVariable Long id) {
        return responsiveService.getEditResponsiveData(id);
    }
}
