package com.grupoeimsa.sigeim.models.history_photos.model.controller;

import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestEquipmentsPaginationDto;
import com.grupoeimsa.sigeim.models.history_photos.model.controller.dto.PhotoHistoryGroupDto;
import com.grupoeimsa.sigeim.models.history_photos.model.controller.dto.UploadHistoryEquipmentPhotosDto;
import com.grupoeimsa.sigeim.models.history_photos.service.HistoryPhotoEquipmentService;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/sigeim/history-photos")
@CrossOrigin(origins = "*")
public class HistoryPhotoEquipmentController {
    private final HistoryPhotoEquipmentService service;

    public HistoryPhotoEquipmentController(HistoryPhotoEquipmentService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> subirFotos(
            @RequestPart("request") UploadHistoryEquipmentPhotosDto request,
            @RequestPart("imagenes") List<MultipartFile> imagenes
    ) {
        try {
            String resultado = service.uploadPhotos(request, imagenes);
            return ResponseEntity.ok(resultado);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir imágenes");
        }
    }

    @PostMapping("/photo-history")
    public ResponseEntity<List<PhotoHistoryGroupDto>> getPhotoHistory(@RequestBody UploadHistoryEquipmentPhotosDto request) {
        List<PhotoHistoryGroupDto> history = service.getPhotoHistoryByEquipmentId(request.getEquipmentId());
        return ResponseEntity.ok(history);
    }


}
