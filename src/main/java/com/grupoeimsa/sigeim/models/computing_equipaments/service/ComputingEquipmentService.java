package com.grupoeimsa.sigeim.models.computing_equipaments.service;

import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestRegisterComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestUpdateComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.CEStatus;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ComputingEquipmentService {
    private final IComputerEquipament repository;
    private final IPerson personRepository;

    public ComputingEquipmentService(IComputerEquipament repository, IPerson personRepository) {
        this.repository = repository;
        this.personRepository = personRepository;
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

}
