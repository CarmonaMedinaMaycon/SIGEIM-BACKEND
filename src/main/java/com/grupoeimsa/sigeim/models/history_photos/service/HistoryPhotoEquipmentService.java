package com.grupoeimsa.sigeim.models.history_photos.service;

import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.history_photos.model.BeanHistoryPhotosEquipament;
import com.grupoeimsa.sigeim.models.history_photos.model.IHistoryPhotosEquipament;
import com.grupoeimsa.sigeim.models.history_photos.model.controller.dto.UploadHistoryEquipmentPhotosDto;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoryPhotoEquipmentService {
    private final IHistoryPhotosEquipament repository;
    private final IComputerEquipament computerEquipmentRepository;

    public HistoryPhotoEquipmentService(IHistoryPhotosEquipament repository, IComputerEquipament computerEquipmentRepository) {
        this.repository = repository;
        this.computerEquipmentRepository = computerEquipmentRepository;
    }

//    public String uploadPhotos(UploadHistoryEquipmentPhotosDto request, List<MultipartFile> photos) throws IOException {
//        BeanComputerEquipament equipament = computerEquipmentRepository.findById(request.getEquipmentId())
//                .orElseThrow(() -> new CustomException("Equipment not found"));
//
//        String personName = equipament.getPerson().getFullName();
//
//        if (photos.size() > 9) {
//            throw new CustomException("Too many photos");
//        }
//
//        List<BeanHistoryPhotosEquipament> photosToSave = new ArrayList<>();
//
//        for (MultipartFile photo : photos) {
//            if (photo.getSize() > 8 * 1024 * 1024) {
//                throw new CustomException("El archivo " + photo.getOriginalFilename() + " supera el límite de 8MB.");
//            }
//
//            BeanHistoryPhotosEquipament historyPhoto = new BeanHistoryPhotosEquipament();
//            historyPhoto.setComputerEquipament(equipament);
//            historyPhoto.setPhoto(photo.getBytes());
//            historyPhoto.setPersonName(personName);
//            historyPhoto.setDate(LocalDate.now());
//
//            photosToSave.add(historyPhoto);
//        }
//
//        repository.saveAll(photosToSave);
//        return "Fotos registradas";
//    }
//
//    public Map<String, Map<String, List<String>>> getGroupedPhotosByEquipment(Long equipmentId) {
//        List<BeanHistoryPhotosEquipament> photos = repository.findByComputerEquipamentId(equipmentId);
//
//        return photos.stream()
//                .collect(Collectors.groupingBy(
//                        BeanHistoryPhotosEquipament::getPersonName,
//                        Collectors.groupingBy(
//                                photo -> photo.getDate().toString(),
//                                Collectors.mapping(
//                                        photo -> Base64.getEncoder().encodeToString(photo.getPhoto()),
//                                        Collectors.toList()
//                                )
//                        )
//                ));
//    }


    public String uploadPhotos(UploadHistoryEquipmentPhotosDto request, List<MultipartFile> photos) throws IOException {
        BeanComputerEquipament equipament = computerEquipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new CustomException("Equipment not found"));

        String personName = equipament.getPerson().getFullName();

        if (photos.size() > 9) {
            throw new CustomException("Too many photos");
        }

        String uploadDir = "/opt/uploads/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        List<BeanHistoryPhotosEquipament> photosToSave = new ArrayList<>();

        for (MultipartFile photo : photos) {
            if (photo.getSize() > 8 * 1024 * 1024) {
                throw new CustomException("El archivo " + photo.getOriginalFilename() + " supera el límite de 8MB.");
            }

            // Generar nombre único para el archivo
            String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
            String fullPath = uploadDir + fileName;

            // Guardar archivo en disco
            File destFile = new File(fullPath);
            photo.transferTo(destFile);

            // Guardar solo la ruta en la base de datos
            BeanHistoryPhotosEquipament historyPhoto = new BeanHistoryPhotosEquipament();
            historyPhoto.setComputerEquipament(equipament);
            historyPhoto.setPhotos(fullPath); // Asumiendo que ahora guardas una ruta en vez de un byte[]
            historyPhoto.setPersonName(personName);
            historyPhoto.setDate(LocalDate.now());

            photosToSave.add(historyPhoto);
        }

        repository.saveAll(photosToSave);
        return "Fotos registradas";
    }


}
