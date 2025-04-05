package com.grupoeimsa.sigeim.models.responsives.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.DownloadResponsiveDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.GenerateResponsiveCellphoneDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.GenerateResponsiveDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.RequestSearchResponsiveEquipmentsDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseEditResponsiveEquipmentDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseResponsiveCellphonesDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseResponsiveEquipmentsDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.UpdateResponsiveDto;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveEquipaments;
import com.grupoeimsa.sigeim.models.responsives.model.IResponsiveEquipments;
import com.grupoeimsa.sigeim.models.responsives.service.ResponsiveService;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("api/sigeim/responsives")
@CrossOrigin(origins = {"*"})
public class ResponsiveController {

    private final ResponsiveService responsiveService;

    private final IResponsiveEquipments responsiveEquipmentsRepository;

    public ResponsiveController(ResponsiveService responsiveService, IResponsiveEquipments responsiveEquipmentsRepository) {
        this.responsiveService = responsiveService;
        this.responsiveEquipmentsRepository = responsiveEquipmentsRepository;
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

    @PostMapping("/generate-cellphone-responsive")
    public ResponseEntity<String> generateResponsiveCellphone(@RequestBody GenerateResponsiveCellphoneDto dto) {
        try {
            responsiveService.generateResponsiveCellphone(dto);
            return ResponseEntity.ok("Responsiva de celular generada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al generar la responsiva: " + e.getMessage());
        }
    }

    @PostMapping("/equipments")
    public ResponseEntity<Page<ResponseResponsiveEquipmentsDto>> getResponsivesEquipments(
            @RequestBody RequestSearchResponsiveEquipmentsDto dto) {
        Page<ResponseResponsiveEquipmentsDto> result = responsiveService.getResponsivesEquipments(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cellphones")
    public ResponseEntity<Page<ResponseResponsiveCellphonesDto>> getResponsivesCellphones(
            @RequestBody RequestSearchResponsiveEquipmentsDto dto) {
        Page<ResponseResponsiveCellphonesDto> result = responsiveService.getResponsivesCellphones(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/equipments/download")
    public ResponseEntity<byte[]> downloadResponsive(@RequestBody DownloadResponsiveDto dto) {
        return responsiveService.downloadResponsive(dto);
    }

    @PostMapping("/cellphones/download")
    public ResponseEntity<byte[]> downloadResponsiveCellphone(@RequestBody DownloadResponsiveDto dto) {
        return responsiveService.downloadResponsiveCellphone(dto);
    }

    @GetMapping("/get-edit-data/{id}")
    public ResponseEntity<ResponseEditResponsiveEquipmentDto> getEditResponsive(@PathVariable Long id) {
        return responsiveService.getEditResponsiveData(id);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateResponsive(@RequestBody UpdateResponsiveDto dto) {
        try {
            responsiveService.updateResponsive(dto);
            return ResponseEntity.ok("Responsiva actualizada correctamente.");
        } catch (Exception e) {
            e.printStackTrace(); // 👈 asegúrate de imprimirlo en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar responsiva: " + e.getMessage());
        }
    }

    @PostMapping("/equipments/upload-signed")
    public ResponseEntity<?> uploadSignedDoc(
            @RequestParam("file") MultipartFile file,
            @RequestParam("data") String jsonData) {
        try {
            // Parseamos el JSON que contiene el ID
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonData);
            Long responsiveId = node.get("responsiveId").asLong();

            responsiveService.uploadSignedDoc(responsiveId, file);
            return ResponseEntity.ok("Archivo firmado cargado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir archivo firmado: " + e.getMessage());
        }
    }

    @PostMapping("/equipments/download-signed")
    public ResponseEntity<byte[]> downloadSignedDoc(@RequestBody Map<String, Long> request) {
        Long id = request.get("responsiveId");
        BeanResponsiveEquipaments responsive = responsiveEquipmentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responsiva no encontrada"));

        byte[] file = responsive.getSignedDoc();
        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("responsiva_firmada.pdf").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(file);
    }

    @PostMapping("/equipments/cancel")
    public ResponseEntity<Void> cancelResponsive(@RequestBody Map<String, Long> payload) {
        Long responsiveId = payload.get("responsiveId");
        responsiveService.cancelResponsive(responsiveId);
        return ResponseEntity.ok().build();
    }

}
