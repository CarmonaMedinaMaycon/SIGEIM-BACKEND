package com.grupoeimsa.sigeim.models.history_photos.model.controller;

import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestEquipmentsPaginationDto;
import com.grupoeimsa.sigeim.models.history_photos.model.controller.dto.UploadHistoryEquipmentPhotosDto;
import com.grupoeimsa.sigeim.models.history_photos.service.HistoryPhotoEquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/sigeim/history-photos")
public class HistoryPhotoEquipmentController {
    private final HistoryPhotoEquipmentService service;

    public HistoryPhotoEquipmentController(HistoryPhotoEquipmentService service) {
        this.service = service;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPhotos(
            @RequestPart("request") UploadHistoryEquipmentPhotosDto request,
            @RequestPart("photos") List<MultipartFile> photos) throws IOException {
        String response = service.uploadPhotos(request, photos);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/photos")
    public ResponseEntity<Map<String, Map<String, List<String>>>> getPhotosByEquipmentId(@RequestBody UploadHistoryEquipmentPhotosDto request) {
        Map<String, Map<String, List<String>>> groupedPhotos = service.getGroupedPhotosByEquipment(request.getEquipmentId());
        return ResponseEntity.ok(groupedPhotos);
    }


}
