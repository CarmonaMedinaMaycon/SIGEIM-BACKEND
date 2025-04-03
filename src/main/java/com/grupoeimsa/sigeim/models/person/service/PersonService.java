package com.grupoeimsa.sigeim.models.person.service;

import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.cellphones.model.ICellphone;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.person.controller.dto.*;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveEquipaments;
import com.grupoeimsa.sigeim.models.responsives.model.IResponsiveEquipments;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonService {

    public final IPerson personRepository;
    public final IComputerEquipament computerEquipamentRepository;
    public final ICellphone cellphoneRepository;
    public final IResponsiveEquipments responsiveEquipmentsRepository;

    public PersonService(IPerson personRepository, IComputerEquipament computerEquipamentRepository, ICellphone cellphoneRepository, IResponsiveEquipments responsiveEquipmentsRepository) {
        this.personRepository = personRepository;
        this.computerEquipamentRepository = computerEquipamentRepository;
        this.cellphoneRepository = cellphoneRepository;
        this.responsiveEquipmentsRepository = responsiveEquipmentsRepository;
    }

    @Transactional(readOnly = true)
    public Page<ResponsePersonDTO> findAll(String search, int page, int size, Boolean status, String enterprise, String departament) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BeanPerson> person = personRepository.findAllByFilters(
                search,
                departament,
                enterprise,
                status,
                pageable
        );
        if (person.isEmpty()) {
            throw new CustomException("No persons were found");
        }
        return person.map(ResponsePersonDTO::new);
    }

    @Transactional(readOnly = true)
    public ResponsePersonDTO findById(Long id) {
        BeanPerson person = personRepository.findById(id).orElseThrow(() -> new CustomException("The user was not found"));
        return new ResponsePersonDTO(person);

    }

    @Transactional(rollbackFor = {SQLException.class})
    public void registerPersonal(ResponseRegisterPersonDTO responsePersonDTO) {
        BeanPerson person = new BeanPerson();
        person.setName(responsePersonDTO.getName());
        person.setSurname(responsePersonDTO.getSurname());
        person.setLastname(responsePersonDTO.getLastname());
        person.setWhoRegistered(responsePersonDTO.getWhoRegistered());
        person.setEmailRegistered(responsePersonDTO.getEmailRegistered());
        person.setEmail(responsePersonDTO.getEmail());
        if (personRepository.existsByEmail(person.getEmail())) {
            throw new CustomException("email already exists");
        }
        person.setPhoneNumber(responsePersonDTO.getPhoneNumber());
        person.setDepartament(responsePersonDTO.getDepartament());
        person.setEnterprise(responsePersonDTO.getEnterprise());
        person.setPosition(responsePersonDTO.getPosition());
        person.setComments(responsePersonDTO.getComments());
        person.setCommentsHardwareSoftware(responsePersonDTO.getCommentsHardwareSoftware());
        person.setCommentsEmail(responsePersonDTO.getCommentsEmail());
        person.setDateStart(responsePersonDTO.getDateStart());
        person.setDateEnd(responsePersonDTO.getDateEnd());
        person.setEntryDate(responsePersonDTO.getEntryDate());
        person.setStatus(true);
        personRepository.save(person);
    }

    @Transactional
    public void enableDisable(Long id) {
        // Buscar la persona por ID, lanzar excepción si no se encuentra
        BeanPerson person = personRepository.findById(id)
                .orElseThrow(() -> new CustomException("Person not found"));

        person.setDateEnd(LocalDate.now());

        // Obtener la persona por defecto con ID 1 (para evitar múltiples consultas)
        BeanPerson defaultPerson = personRepository.findById(1L)
                .orElseThrow(() -> new CustomException("Default person not found"));

        // Validar si la persona tiene equipos asignados
        if (!person.getComputerEquipaments().isEmpty()) {
            // Iterar sobre todos los equipos y reasignarlos
            for (BeanComputerEquipament equipament : person.getComputerEquipaments()) {
                BeanComputerEquipament computerEquipament = computerEquipamentRepository.findById(
                        equipament.getComputerEquipamentId()
                ).orElseThrow(() -> new CustomException("Computer equipment not found"));

                computerEquipament.setPerson(defaultPerson);
                computerEquipamentRepository.save(computerEquipament);
            }
        }

        // Validar si la persona tiene celulares asignados
        if (!person.getCellphone().isEmpty()) {
            // Iterar sobre todos los celulares y reasignarlos
            for (BeanCellphone cell : person.getCellphone()) {
                BeanCellphone cellphone = cellphoneRepository.findById(
                        cell.getCellphoneId()
                ).orElseThrow(() -> new CustomException("Cellphone not found"));

                cellphone.setPerson(defaultPerson);
                cellphoneRepository.save(cellphone);
            }
        }


        // Cambiar el estado de la persona
        person.setStatus(!person.getStatus());
        personRepository.save(person);
    }


    @Transactional
    public void update(ResponseUpdatePersonDTO updatePersonDTO) {
        BeanPerson person =  personRepository.findById(updatePersonDTO.getPersonId())
                .orElseThrow(() -> new CustomException("Person not found"));
        person.setName(updatePersonDTO.getName());
        person.setSurname(updatePersonDTO.getSurname());
        person.setLastname(updatePersonDTO.getLastname());
        person.setEmail(updatePersonDTO.getEmail());
        person.setPhoneNumber(updatePersonDTO.getPhoneNumber());
        person.setDepartament(updatePersonDTO.getDepartament());
        person.setEnterprise(updatePersonDTO.getEnterprise());
        person.setPosition(updatePersonDTO.getPosition());
        person.setComments(updatePersonDTO.getComments());
        person.setEmailRegistered(updatePersonDTO.getEmailRegistered());
        person.setWhoRegistered(updatePersonDTO.getWhoRegistered());
        person.setCommentsHardwareSoftware(updatePersonDTO.getCommentsHardwareSoftware());
        person.setCommentsEmail(updatePersonDTO.getCommentsEmail());
        personRepository.save(person);
    }

    public List<ResponseResponsibleSelectDto> getAllPersonsForSelect() {
        List<BeanPerson> persons = personRepository.findAll();

        return persons.stream()
                .filter(BeanPerson::getStatus)
                .map(person -> new ResponseResponsibleSelectDto(
                        person.getPersonId(),
                        person.getFullName()
                ))
                .collect(Collectors.toList());
    }

    public List<ResponseLicencesPersonSelectDto> getAllPersonsForSelectInLicenses() {
        List<BeanPerson> persons = personRepository.findAll();

        return persons.stream()
                .filter(BeanPerson::getStatus)
                .map(person -> new ResponseLicencesPersonSelectDto(
                        person.getPersonId(),
                        person.getFullName(),
                        person.getLicense() != null,
                        person.getDepartament(),
                        person.getPhoneNumber()
                ))
                .collect(Collectors.toList());
    }

    public List<ReponsePersonWithPhoneDetailsDto> findAllWithDetails(RequestPersonDTO request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<BeanPerson> personasPage = personRepository.findCustomFiltered(
                request.getSearch(),
                request.getDepartament(),
                request.getEnterprise(),
                request.getStatus(),
                pageable
        );

        List<BeanPerson> personas = personasPage.getContent();

        return personas.stream().map(p -> new ReponsePersonWithPhoneDetailsDto(
                p.getPersonId(),
                p.getFullName(),
                p.getDepartament(),
                p.getEnterprise(),
                p.getLicense() != null,
                p.getCellphone() != null && !p.getCellphone().isEmpty()
        )).toList();
    }

    public List<ResponsePersonWithoutAccessCardDto> findAllWithoutAccessCard(RequestPersonDTO request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<BeanPerson> personasPage = personRepository.findCustomFiltered(
                request.getSearch(),
                request.getDepartament(),
                request.getEnterprise(),
                request.getStatus(),
                pageable
        );

        List<BeanPerson> personas = personasPage.getContent();

        return personas.stream()
                .filter(p -> p.getAccessCard() == null)
                .map(p -> new ResponsePersonWithoutAccessCardDto(
                        p.getPersonId(),
                        p.getFullName(),
                        p.getDepartament(),
                        p.getEnterprise(),
                        false // No tiene tarjeta
                ))
                .toList();
    }






    public List<ResponsePersonSelectDto> getAllPersonsForResponsiveEquipmentGeneration() {
        List<BeanPerson> persons = personRepository.findAll();

        return persons.stream()
                .filter(person -> !"Sistemas NA NA".equalsIgnoreCase(person.getFullName()))
                .filter(BeanPerson::getStatus)
                .map(person -> new ResponsePersonSelectDto(
                        person.getPersonId(),
                        person.getFullName(),
                        person.getDepartament(),
                        person.getPosition()
                ))
                .collect(Collectors.toList());
    }

    public Page<ResponseTablePeopleDto> getPeopleForTable(String search, String departament, String enterprise, Boolean status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Page<BeanPerson> persons = personRepository.findCustomFiltered(
                search != null ? search : "",
                departament != null ? departament : "",
                enterprise != null ? enterprise : "",
                status,
                pageable
        );

        List<ResponseTablePeopleDto> resultList = persons.getContent().stream().map(person -> {
            List<String> serials = person.getComputerEquipaments() != null
                    ? person.getComputerEquipaments().stream()
                    .map(BeanComputerEquipament::getSerialNumber)
                    .collect(Collectors.toList())
                    : List.of();

            return new ResponseTablePeopleDto(
                    person.getPersonId(),
                    person.getName() + " " + person.getSurname() + " " + person.getLastname(),
                    person.getEnterprise(),
                    person.getDepartament(),
                    person.getPhoneNumber(),
                    person.getStatus(),
                    serials
            );
        }).collect(Collectors.toList());

        return new PageImpl<>(resultList, pageable, persons.getTotalElements());
    }


}
