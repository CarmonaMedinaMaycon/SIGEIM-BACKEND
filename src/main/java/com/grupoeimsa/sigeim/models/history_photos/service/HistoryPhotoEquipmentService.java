package com.grupoeimsa.sigeim.models.history_photos.service;

import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.CEStatus;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.history_photos.model.BeanHistoryPhotosEquipament;
import com.grupoeimsa.sigeim.models.history_photos.model.IHistoryPhotosEquipament;
import com.grupoeimsa.sigeim.models.history_photos.model.controller.dto.PhotoHistoryGroupDto;
import com.grupoeimsa.sigeim.models.history_photos.model.controller.dto.UploadHistoryEquipmentPhotosDto;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoryPhotoEquipmentService {
    private final IHistoryPhotosEquipament repository;
    private final IPerson personRepository;
    private final IComputerEquipament computerEquipmentRepository;

    public HistoryPhotoEquipmentService(IHistoryPhotosEquipament repository, IPerson personRepository, IComputerEquipament computerEquipmentRepository) {
        this.repository = repository;
        this.personRepository = personRepository;
        this.computerEquipmentRepository = computerEquipmentRepository;
    }

    public String uploadPhotos(UploadHistoryEquipmentPhotosDto request, List<MultipartFile> photos) throws IOException {
        BeanComputerEquipament equipament = computerEquipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new CustomException("Equipment not found"));

        // 2. Obtener la persona de Sistemas (ID 1)
        BeanPerson sistemasPerson = personRepository.findById(1L)
                .orElseThrow(() -> new CustomException("Persona de Sistemas no encontrada"));

        String personName = equipament.getPerson().getFullName();

        if (photos.size() > 9) {
            throw new CustomException("Too many photos");
        }
        equipament.setPerson(sistemasPerson);
        equipament.setStatus(CEStatus.DISPONIBLE);
        equipament.setDepartament("Administración");
        computerEquipmentRepository.save(equipament);

        List<BeanHistoryPhotosEquipament> existingPhotos =
                repository.findByComputerEquipamentId(request.getEquipmentId());

        Map<Long, List<BeanHistoryPhotosEquipament>> photosByUser = new LinkedHashMap<>();
        for (BeanHistoryPhotosEquipament photo : existingPhotos) {
            Long userId = photo.getComputerEquipament().getPerson().getPersonId();
            photosByUser.computeIfAbsent(userId, k -> new ArrayList<>()).add(photo);
        }

        Long currentUserId = equipament.getPerson().getPersonId();
        if (photosByUser.size() >= 3 && !photosByUser.containsKey(currentUserId)) {
            BeanHistoryPhotosEquipament oldest = existingPhotos.get(0);

            // Borrar el archivo físico
            File oldestFile = new File(oldest.getPhotos());
            if (oldestFile.exists()) {
                oldestFile.delete();
            }

            repository.delete(oldest);
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

            String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
            String fullPath = uploadDir + fileName;

            File destFile = new File(fullPath);
            photo.transferTo(destFile);

            BeanHistoryPhotosEquipament historyPhoto = new BeanHistoryPhotosEquipament();
            historyPhoto.setComputerEquipament(equipament);
            historyPhoto.setPhotos(fullPath);
            historyPhoto.setPersonName(personName);
            historyPhoto.setDate(LocalDate.now());

            photosToSave.add(historyPhoto);
        }

        repository.saveAll(photosToSave);
        return "Fotos registradas";
    }


    public List<PhotoHistoryGroupDto> getPhotoHistoryByEquipmentId(Long equipmentId) {
        List<BeanHistoryPhotosEquipament> allPhotos =
                repository.findByComputerEquipamentId(equipmentId);

        Map<String, List<BeanHistoryPhotosEquipament>> grouped = allPhotos.stream()
                .collect(Collectors.groupingBy(p -> p.getDate() + "::" + p.getPersonName()));

        return grouped.entrySet().stream()
                .map(entry -> {
                    String[] keyParts = entry.getKey().split("::");
                    LocalDate date = LocalDate.parse(keyParts[0]);
                    String personName = keyParts[1];

                    List<String> photoPaths = entry.getValue().stream()
                            .map(photo -> {
                                String fileName = Paths.get(photo.getPhotos()).getFileName().toString();
                                return "http://192.168.2.130:8081/uploads/" + fileName;
                            })
                            .collect(Collectors.toList());

                    return new PhotoHistoryGroupDto(personName, date, photoPaths);
                })
                .sorted(Comparator.comparing(PhotoHistoryGroupDto::getDate).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

}
