package com.grupoeimsa.sigeim.models.computing_equipaments.service;

import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestRegisterComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.CEStatus;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        equipment.setPlace(dto.getPlace());
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
        equipment.setSystemObservations(dto.getSystemObservations());
        repository.save(equipment);
        return "Equipo registrado con exito";
    }

}
