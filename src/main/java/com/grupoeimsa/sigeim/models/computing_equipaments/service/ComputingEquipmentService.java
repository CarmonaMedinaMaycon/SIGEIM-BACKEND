package com.grupoeimsa.sigeim.models.computing_equipaments.service;

import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestRegisterComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestSearchByFilteringEquipmentsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestUpdateComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.ResponseSeeAllEquipmentsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.ResponseSeeDetailsEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.CEStatus;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.history_photos.model.BeanHistoryPhotosEquipament;
import com.grupoeimsa.sigeim.models.history_photos.model.controller.dto.HistoryEquipmentPhotosGroupDto;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComputingEquipmentService {
    private final IComputerEquipament repository;
    private final IPerson personRepository;
    private final BeanHistoryPhotosEquipament beanHistoryPhotosEquipament;

    public ComputingEquipmentService(IComputerEquipament repository, IPerson personRepository, BeanHistoryPhotosEquipament beanHistoryPhotosEquipament) {
        this.repository = repository;
        this.personRepository = personRepository;
        this.beanHistoryPhotosEquipament = beanHistoryPhotosEquipament;
    }

    @Transactional
    public String createComputingEquipment(RequestRegisterComputingEquipmentDto dto) {
        // Crear la entidad a partir del DTO
        BeanComputerEquipament equipment = new BeanComputerEquipament();
        equipment.setSerialNumber(dto.getSerialNumber());
        equipment.setIdEsset(dto.getIdEsset());
        BeanPerson person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new CustomException("Person not found with ID: " + dto.getPersonId()));
        equipment.setPerson(person);
        equipment.setDepartament(dto.getDepartament());
        equipment.setEnterprise(dto.getEnterprise());
        equipment.setWorkModality(dto.getWorkModality());
        equipment.setType(dto.getType());
        equipment.setBrand(dto.getBrand());
        equipment.setModel(dto.getModel());
        equipment.setRamMemoryCapacity(dto.getRamMemoryCapacity());
        equipment.setMemoryCapacity(dto.getMemoryCapacity());
        equipment.setProcessor(dto.getProcessor());
        equipment.setPurchasingCompany(dto.getPurchasingCompany());
        equipment.setHasInvoice(dto.getHasInvoice());
        equipment.setSupplier(dto.getSupplier());
        equipment.setInvoiceFolio(dto.getInvoiceFolio());
        equipment.setPurchaseDate(dto.getPurchaseDate());
        equipment.setAssetNumber(dto.getAssetNumber());
        equipment.setPrice(dto.getPrice());
        switch (dto.getStatus()){
            case 1:
                equipment.setStatus(CEStatus.OCUPADO);
                break;
            case 2:
                equipment.setStatus(CEStatus.DISPONIBLE);
                break;
            case 3:
                equipment.setStatus(CEStatus.CAMBIO_DISCO_AMPLIACION_RAM);
                break;
            case 4:
                equipment.setStatus(CEStatus.BAJA);
                break;
            case 5:
                equipment.setStatus(CEStatus.REPARACION);
                break;
            default:
                throw new CustomException("Status is not valid");
        }
        equipment.setCreationDate(LocalDate.now());
        equipment.setSystemObservations(dto.getSystemObservations());
        repository.save(equipment);
        return "Equipo registrado con exito";
    }

    @Transactional
    public String updateComputingEquipment(RequestUpdateComputingEquipmentDto dto) {
        // Buscar el equipo usando el id que viene en el DTO
        BeanComputerEquipament equipment = repository.findById(dto.getId())
                .orElseThrow(() -> new CustomException("Equipo no encontrado con ID: " + dto.getId()));

        // Validar existencia del responsable
        BeanPerson person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new CustomException("Responsable no encontrado con ID: " + dto.getPersonId()));

        // Actualizar los datos del equipo
        equipment.setSerialNumber(dto.getSerialNumber());
        equipment.setIdEsset(dto.getIdEsset());
        equipment.setPerson(person);
        equipment.setDepartament(dto.getDepartament());
        equipment.setEnterprise(dto.getEnterprise());
        equipment.setWorkModality(dto.getWorkModality());
        equipment.setType(dto.getType());
        equipment.setBrand(dto.getBrand());
        equipment.setModel(dto.getModel());
        equipment.setRamMemoryCapacity(dto.getRamMemoryCapacity());
        equipment.setMemoryCapacity(dto.getMemoryCapacity());
        equipment.setProcessor(dto.getProcessor());
        equipment.setPurchasingCompany(dto.getPurchasingCompany());
        equipment.setSupplier(dto.getSupplier());
        equipment.setInvoiceFolio(dto.getInvoiceFolio());
        equipment.setPurchaseDate(dto.getPurchaseDate());
        equipment.setAssetNumber(dto.getAssetNumber());
        equipment.setPrice(dto.getPrice());
        equipment.setSystemObservations(dto.getSystemObservations());

        // Actualizar la fecha de última modificación
        equipment.setLastUpdateDate(LocalDate.now());

        repository.save(equipment);
        return "Equipo actualizado con éxito";
    }

    public Page<ResponseSeeAllEquipmentsDto> getAllEquipments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<BeanComputerEquipament> equipmentPage = repository.findAll(pageable);

        return equipmentPage.map(equipment -> {
            ResponseSeeAllEquipmentsDto dto = new ResponseSeeAllEquipmentsDto();
            dto.setSerialNumber(equipment.getSerialNumber());
            dto.setIdEsset(equipment.getIdEsset());
            dto.setResponsibleName(equipment.getPerson().getName() + " " + equipment.getPerson().getSurname());
            dto.setDepartament(equipment.getDepartament());
            dto.setType(equipment.getType());
            dto.setBrand(equipment.getBrand());
            dto.setEquipmentStatus(equipment.getStatus().toString());
            return dto;
        });
    }

    public List<ResponseSeeAllEquipmentsDto> searchEquipments(ResponseSeeAllEquipmentsDto searchCriteria) {
        // Convertir equipmentStatus de String a CEStatus
        CEStatus equipmentStatus = null;
        if (searchCriteria.getEquipmentStatus() != null) {
            try {
                equipmentStatus = CEStatus.valueOf(searchCriteria.getEquipmentStatus());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status value: " + searchCriteria.getEquipmentStatus());
            }
        }

        // Obtener la lista de equipos
        List<BeanComputerEquipament> equipos = repository.findByFilters(
                searchCriteria.getSerialNumber(),
                searchCriteria.getIdEsset(),
                searchCriteria.getResponsibleName(),
                searchCriteria.getDepartament(),
                searchCriteria.getType(),
                searchCriteria.getBrand(),
                equipmentStatus
        );

        // Convertir la lista de BeanComputerEquipament a ResponseSeeAllEquipmentsDto
        List<ResponseSeeAllEquipmentsDto> result = new ArrayList<>();
        for (BeanComputerEquipament equipo : equipos) {
            ResponseSeeAllEquipmentsDto dto = getResponseSeeAllEquipmentsDto(equipo);
            // Agregar el DTO a la lista de resultados
            result.add(dto);
        }

        return result;
    }

    private ResponseSeeAllEquipmentsDto getResponseSeeAllEquipmentsDto(BeanComputerEquipament equipo) {
        ResponseSeeAllEquipmentsDto dto = new ResponseSeeAllEquipmentsDto();
        // Mapear los campos de BeanComputerEquipament a ResponseSeeAllEquipmentsDto
        dto.setSerialNumber(equipo.getSerialNumber());
        dto.setIdEsset(equipo.getIdEsset());
        dto.setResponsibleName(equipo.getPerson() != null ? equipo.getPerson().getLastname() + " " + equipo.getPerson().getSurname() : "");
        dto.setDepartament(equipo.getDepartament());
        dto.setType(equipo.getType());
        dto.setBrand(equipo.getBrand());
        dto.setEquipmentStatus(equipo.getStatus().name()); // Si es un enum, convierte a String
        return dto;
    }

    public Map<String, List<String>> getAvailableFilters() {
        Map<String, List<String>> filters = new HashMap<>();

        filters.put("tipos", repository.findDistinctTypes());
        filters.put("proveedores", repository.findDistinctSuppliers());
        filters.put("estados", Arrays.stream(CEStatus.values()).map(Enum::name).toList());
        filters.put("marcas", repository.findDistinctBrands());

        return filters;
    }

    public Page<ResponseSeeAllEquipmentsDto> searchEquipments(RequestSearchByFilteringEquipmentsDto filtros) {
        CEStatus equipmentStatus = null;
        if (filtros.getEquipmentStatus() != null && !filtros.getEquipmentStatus().equalsIgnoreCase("Todos")) {
            try {
                equipmentStatus = CEStatus.valueOf(filtros.getEquipmentStatus());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status value: " + filtros.getEquipmentStatus());
            }
        }

        // Valores por defecto si no se envían en el body
        int page = (filtros.getPage() != null) ? filtros.getPage() : 0;
        int size = (filtros.getSize() != null) ? filtros.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "creationDate"));

        Page<BeanComputerEquipament> equipos = repository.findByFilters(
                Optional.ofNullable(filtros.getType()).filter(type -> !type.equalsIgnoreCase("Todos")).orElse(null),
                Optional.ofNullable(filtros.getSupplier()).filter(supplier -> !supplier.equalsIgnoreCase("Todos")).orElse(null),
                Optional.ofNullable(filtros.getBrand()).filter(brand -> !brand.equalsIgnoreCase("Todos")).orElse(null),
                equipmentStatus,
                pageable
        );

        return equipos.map(this::getResponseSeeAllEquipmentsDto);
    }

    public ResponseSeeDetailsEquipmentDto getEquipamentDetails(Long id) {
        // Fetching the equipment by id
        BeanComputerEquipament equipo = repository.findById(id)
                .orElseThrow(() -> new CustomException("Equipment not found with id: " + id));

        // Agrupar fotos por fecha y responsable
        Map<String, List<String>> groupedPhotos = equipo.getHistoryPhotosEquipament().stream()
                .collect(Collectors.groupingBy(
                        p -> p.getDate() + " - " + p.getPersonName(),
                        Collectors.mapping(BeanHistoryPhotosEquipament::getPhoto, Collectors.toList())
                ));

        List<HistoryEquipmentPhotosGroupDto> historyPhotos = groupedPhotos.entrySet().stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split(" - ");
                    return new HistoryEquipmentPhotosGroupDto(LocalDate.parse(parts[0]), parts[1], entry.getValue());
                })
                .collect(Collectors.toList());

        return new ResponseSeeDetailsEquipmentDto(
                equipo.getSerialNumber(),
                equipo.getIdEsset(),
                equipo.getPerson().getFullName(),
                equipo.getDepartament(),
                equipo.getEnterprise(),
                equipo.getWorkModality(),
                equipo.getType(),
                equipo.getBrand(),
                equipo.getModel(),
                equipo.getRamMemoryCapacity(),
                equipo.getMemoryCapacity(),
                equipo.getProcessor(),
                equipo.getPurchasingCompany(),
                equipo.getHasInvoice(),
                equipo.getSupplier(),
                equipo.getInvoiceFolio(),
                equipo.getPurchaseDate(),
                equipo.getAssetNumber(),
                equipo.getPrice(),
                equipo.getStatus().toString(),
                equipo.getSystemObservations(),
                equipo.getCreationDate(),
                equipo.getLastUpdateDate(),
                historyPhotos
        );
    }




}
