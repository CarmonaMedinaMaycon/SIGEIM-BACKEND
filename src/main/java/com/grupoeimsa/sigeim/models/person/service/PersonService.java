package com.grupoeimsa.sigeim.models.person.service;

import com.grupoeimsa.sigeim.models.acess_cards.model.BeanAccessCard;
import com.grupoeimsa.sigeim.models.acess_cards.model.IAcessCard;
import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.cellphones.model.ICellphone;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.CEStatus;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.licenses.model.BeanLicense;
import com.grupoeimsa.sigeim.models.licenses.model.ILicense;
import com.grupoeimsa.sigeim.models.person.controller.dto.*;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveCards;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveEquipaments;
import com.grupoeimsa.sigeim.models.responsives.model.EStatus;
import com.grupoeimsa.sigeim.models.responsives.model.IResponsiveEquipments;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import com.grupoeimsa.sigeim.security.service.VerificationCodeInfo;
import com.grupoeimsa.sigeim.utils.CustomException;
import com.grupoeimsa.sigeim.utils.SessionInformation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonService {

    public final IPerson personRepository;
    public final IComputerEquipament computerEquipamentRepository;
    public final ICellphone cellphoneRepository;
    public final IResponsiveEquipments responsiveEquipmentsRepository;
    public final ILicense licenseRepository;
    public final IAcessCard acessCardRepository;
    private final JavaMailSender mailSender;


    public PersonService(IPerson personRepository, JavaMailSender mailSender, IAcessCard acessCardRepository, ILicense licenseRepository, IComputerEquipament computerEquipamentRepository, ICellphone cellphoneRepository, IResponsiveEquipments responsiveEquipmentsRepository) {
        this.personRepository = personRepository;
        this.computerEquipamentRepository = computerEquipamentRepository;
        this.cellphoneRepository = cellphoneRepository;
        this.responsiveEquipmentsRepository = responsiveEquipmentsRepository;
        this.licenseRepository = licenseRepository;
        this.acessCardRepository = acessCardRepository;
        this.mailSender = mailSender;
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
        person.setPhoneNumberAssigned(responsePersonDTO.getPhoneNumberAssigned());
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
        person.setExecutiveCode(responsePersonDTO.getExecutiveCode());

        sendNotification(person.getFullName(), person.getWhoRegistered(), person.getDateEnd());

        personRepository.save(person);
    }

    public void sendNotification(String userName, String whoRegistered, String dateEnd) {
        sendEmail("egonzalez@interferenciales.com.mx", "Notificación de registro de usuario - SIGEIM",
                "Saludos Daniel se te informa que un nuevo usuario ("+userName+")"+ " ha sido registrado en el sistema por " +
                        whoRegistered + " el registro finalizó: " + dateEnd);
    }


    private void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException("Failed to send notification email");
        }
    }

    @Transactional
    public void enableDisable(Long id) {
        // Buscar a la persona
        BeanPerson person = personRepository.findById(id)
                .orElseThrow(() -> new CustomException("Person not found"));

        // No se permite desactivar a "Sistemas"
        if ("Sistemas".equalsIgnoreCase(person.getName())) {
            throw new CustomException("No se puede desactivar a la persona 'Sistemas'.");
        }

        // No permitir reactivación
        if (!person.getStatus()) {
            throw new CustomException("No se puede reactivar un empleado desactivado permanentemente.");
        }

        // Obtener la persona "Sistemas"
        BeanPerson sistemas = personRepository.findByName("Sistemas")
                .orElseThrow(() -> new CustomException("Persona 'Sistemas' no encontrada"));

        // === 1. Cancelar responsivas de tarjeta de acceso y eliminar la tarjeta ===
        if (person.getAccessCard() != null) {
            BeanAccessCard card = person.getAccessCard();

            if (card.getResponsives() != null) {
                card.getResponsives().forEach(responsive -> {
                    if (responsive.getStatus() == EStatus.ACTIVA_POR_FIRMAR || responsive.getStatus() == EStatus.ACTIVA_FIRMADA) {
                        responsive.setStatus(EStatus.CANCELADA);
                    }
                });
            }

            // ⚠️ Romper la relación para que se elimine automáticamente (por orphanRemoval)
            person.setAccessCard(null);
        }

        // === 2. Reasignar celulares a "Sistemas" y cancelar responsivas ===
        if (!person.getCellphone().isEmpty()) {
            for (BeanCellphone cellphone : person.getCellphone()) {
                if (cellphone.getResponsiveCellphones() != null) {
                    cellphone.getResponsiveCellphones().forEach(responsive -> {
                        if (responsive.getStatus() == EStatus.ACTIVA_POR_FIRMAR || responsive.getStatus() == EStatus.ACTIVA_FIRMADA) {
                            responsive.setStatus(EStatus.CANCELADA);
                        }
                    });
                }

                cellphone.setPerson(sistemas);
                cellphoneRepository.save(cellphone);
            }
        }

        // === 3. Dar de baja la licencia y cancelar sus responsivas ===
        if (person.getLicenses() != null && !person.getLicenses().isEmpty()) {
            for (BeanLicense license : person.getLicenses()) {
                license.setStatus(false);

                if (license.getResponsivesLicenses() != null) {
                    license.getResponsivesLicenses().forEach(responsive -> {
                        if (responsive.getStatus() == EStatus.ACTIVA_POR_FIRMAR || responsive.getStatus() == EStatus.ACTIVA_FIRMADA) {
                            responsive.setStatus(EStatus.CANCELADA);
                        }
                    });
                }

                licenseRepository.save(license);
            }
        }


        // === 4. Reasignar equipos, cambiar estado y cancelar responsivas ===
        if (!person.getComputerEquipaments().isEmpty()) {
            for (BeanComputerEquipament equip : person.getComputerEquipaments()) {
                if (equip.getResponsiveEquipaments() != null) {
                    equip.getResponsiveEquipaments().forEach(responsive -> {
                        if (responsive.getStatus() == EStatus.ACTIVA_POR_FIRMAR || responsive.getStatus() == EStatus.ACTIVA_FIRMADA) {
                            responsive.setStatus(EStatus.CANCELADA);
                        }
                    });
                }

                equip.setPerson(sistemas);
                equip.setDepartament(sistemas.getDepartament());
                equip.setStatus(CEStatus.DISPONIBLE);
                computerEquipamentRepository.save(equip);
            }
        }

        // === 5. Desactivar al empleado ===
        person.setStatus(false);
        personRepository.save(person);
    }



    public ResponseEditPersonDto getSimplePersonById(Long id) {
        BeanPerson person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        ResponseEditPersonDto dto = new ResponseEditPersonDto();
        dto.setId(person.getPersonId());
        dto.setName(person.getName());
        dto.setSurname(person.getSurname());
        dto.setLastname(person.getLastname());
        dto.setEmail(person.getEmail());
        dto.setPhoneNumber(person.getPhoneNumber());
        dto.setPhoneNumberAssigned(person.getPhoneNumberAssigned());
        dto.setDepartament(person.getDepartament());
        dto.setWhoRegistered(person.getWhoRegistered());
        dto.setEmailRegistered(person.getEmailRegistered());
        dto.setEnterprise(person.getEnterprise());
        dto.setPosition(person.getPosition());
        dto.setComments(person.getComments());
        dto.setCommentsHardwareSoftware(person.getCommentsHardwareSoftware());
        dto.setCommentsEmail(person.getCommentsEmail());
        dto.setEntryDate(person.getEntryDate());
        dto.setExecutiveCode(person.getExecutiveCode());
        return dto;
    }



    public void updatePerson(ResponseEditPersonDto dto) {
        BeanPerson person = personRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        person.setName(dto.getName());
        person.setSurname(dto.getSurname());
        person.setLastname(dto.getLastname());
        person.setWhoRegistered(dto.getWhoRegistered());
        person.setEmailRegistered(dto.getEmailRegistered());
        person.setEmail(dto.getEmail());
        person.setPhoneNumber(dto.getPhoneNumber());
        person.setPhoneNumberAssigned(dto.getPhoneNumberAssigned());
        person.setDepartament(dto.getDepartament());
        person.setEnterprise(dto.getEnterprise());
        person.setPosition(dto.getPosition());
        person.setComments(dto.getComments());
        person.setCommentsHardwareSoftware(dto.getCommentsHardwareSoftware());
        person.setCommentsEmail(dto.getCommentsEmail());
        person.setEntryDate(dto.getEntryDate());
        person.setExecutiveCode(dto.getExecutiveCode());

        System.out.println("Codigo de empleado recibido: " + person.getExecutiveCode());

        personRepository.save(person);
    }


    public List<ResponseResponsibleSelectDto> getAllPersonsForSelect() {
        List<BeanPerson> persons = personRepository.findAll();

        return persons.stream()
                .filter(BeanPerson::getStatus)
                .map(person -> new ResponseResponsibleSelectDto(
                        person.getPersonId(),
                        person.getFullName(),
                        person.getDepartament()
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
                p.getLicenses() != null,
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
                .filter(p -> p.getAccessCard() == null) // Solo personas SIN tarjeta
                .map(p -> new ResponsePersonWithoutAccessCardDto(
                        p.getPersonId(),
                        p.getFullName(),
                        p.getDepartament(),
                        p.getEnterprise(),
                        false // Confirmamos que no tiene tarjeta
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

    public List<ResponsePersonSelectDto> getPersonsAvailableForResponsiveCards() {
        List<BeanPerson> persons = personRepository.findAll();
        Set<Long> addedPersonIds = new HashSet<>();

        return persons.stream()
                .filter(person -> Boolean.TRUE.equals(person.getStatus()))
                .filter(person -> !"SISTEMAS NA NA".equalsIgnoreCase(person.getFullName()))
                .filter(person -> {
                    BeanAccessCard card = person.getAccessCard();

                    // Si no tiene tarjeta, no pasa
                    if (card == null) return false;

                    // Si la tarjeta tiene responsivas activas o por firmar, no pasa
                    return card.getResponsives().stream()
                            .noneMatch(r ->
                                    r.getStatus() == EStatus.ACTIVA_POR_FIRMAR ||
                                            r.getStatus() == EStatus.ACTIVA_FIRMADA
                            );
                })
                .filter(person -> addedPersonIds.add(person.getPersonId())) // ← evita duplicados
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
                    person.getName() + " " + person.getLastname() + " " + person.getSurname(),
                    person.getEnterprise(),
                    person.getDepartament(),
                    person.getPhoneNumber(),
                    person.getStatus(),
                    person.getExecutiveCode(),
                    serials
            );
        }).collect(Collectors.toList());

        return new PageImpl<>(resultList, pageable, persons.getTotalElements());
    }


    public ResponsePersonalInfoDto getPersonalInfo(Long id) {
        BeanPerson person = personRepository.findById(id)
                .orElseThrow(() -> new CustomException("Empleado no encontrado"));

        return new ResponsePersonalInfoDto(
                person.getPersonId(),
                person.getName(),
                person.getSurname(),
                person.getLastname(),
                person.getEnterprise(),
                person.getDepartament(),
                person.getPosition(),
                person.getEntryDate(),
                person.getPhoneNumber(),
                person.getEmail(),
                person.getExecutiveCode()
        );
    }

    public List<ResponseComputerEquipmentDto> getEquipmentsByPersonId(Long id) {
        BeanPerson person = personRepository.findById(id)
                .orElseThrow(() -> new CustomException("Empleado no encontrado"));

        return person.getComputerEquipaments().stream().map(e -> {
            // Buscar la primera responsiva activa o por firmar
            Optional<BeanResponsiveEquipaments> activeResponsive = e.getResponsiveEquipaments().stream()
                    .filter(r -> r.getStatus() == EStatus.ACTIVA_FIRMADA || r.getStatus() == EStatus.ACTIVA_POR_FIRMAR)
                    .findFirst();

            boolean hasResponsiveActive = activeResponsive.isPresent();
            Long responsiveId = activeResponsive.map(BeanResponsiveEquipaments::getResponsiveEquipamentId).orElse(null);

            return new ResponseComputerEquipmentDto(
                    e.getComputerEquipamentId(),
                    e.getSerialNumber(),
                    e.getIdEsset(),
                    e.getBrand(),
                    e.getModel(),
                    e.getType(),
                    e.getStatus().toString(),
                    e.getAssetNumber(),
                    hasResponsiveActive,
                    responsiveId
            );
        }).collect(Collectors.toList());
    }



    public List<ResponseCellphoneDto> getCellphonesByPersonId(Long id) {
        BeanPerson person = personRepository.findById(id)
                .orElseThrow(() -> new CustomException("Empleado no encontrado"));

        List<BeanCellphone> cellphones = person.getCellphone();
        if (cellphones == null || cellphones.isEmpty()) {
            return Collections.emptyList();
        }

        return cellphones.stream().map(cell -> new ResponseCellphoneDto(
                cell.getImei(),
                cell.getCompany(),
                cell.getShortDialing(),
                cell.getDateRenovation() != null ? cell.getDateRenovation().toString() : "NA",
                cell.getCellphoneId()
        )).collect(Collectors.toList());
    }


    public List<ResponseLicenseDto> getLicensesByPersonId(Long id) {
        List<BeanLicense> licenses = licenseRepository.findByPersonPersonId(id);


        if (licenses == null || licenses.isEmpty()) {
            return Collections.emptyList();
        }

        return licenses.stream().map(license -> {
            ResponseLicenseDto dto = new ResponseLicenseDto();

            // Office
            dto.setOutlook(license.isOutlook());
            dto.setAccountOutlook(license.getAccountOutlook());
            dto.setTypeOutlook(license.getTypeOutlook());
            dto.setAlias(license.getAliasOutlook());
            dto.setMailbox(license.getMailboxOutlook());
            dto.setCommentsOutlook(license.getCommentsOutlook());
            dto.setPhoneNumber(license.getAuthPhoneNumber());
            dto.setTwoFactorAuthenticationName(license.getAuthTwoFactorAuthenticationName());

            // CRM
            dto.setCrm(license.isCrm());
            dto.setUserCrm(license.getUserCrm());
            dto.setTypeCrm(license.getTypeCrm());
            dto.setCommentsCrm(license.getCommentsCrm());

            // Business Central
            dto.setBc(license.isBc());
            dto.setUserBc(license.getUserBc());
            dto.setIdUserBc(license.getIdUserBc());
            dto.setTypeBc(license.getTypeBc());
            dto.setEnterpriseBc(license.getEnterpriseBc());

            // PureCloud
            dto.setPurecloud(license.isPurecloud());
            dto.setUserPureCloud(license.getUserPureCloud());
            dto.setIdUserPureCloud(license.getIdUserPureCloud());

            // RPA
            dto.setRpa(license.isRpa());
            dto.setUserRpa(license.getUserRpa());
            dto.setModuleRpa(license.getModuleRpa());
            dto.setEnterpriseRpa(license.getEnterpriseRpa());

            // Herramientas adicionales
            dto.setTactical(license.isTactical());

            // Redes Sociales
            dto.setInstagram(license.isInstagram());
            dto.setUserInstagram(license.getUserInstagram());
            dto.setFacebook(license.isFacebook());
            dto.setUserFacebook(license.getUserFacebook());
            dto.setTiktok(license.isTiktok());
            dto.setUserTiktok(license.getUserTiktok());
            dto.setLinkedin(license.isLinkedin());
            dto.setUserLinkedin(license.getUserLinkedin());
            dto.setYoutube(license.isYoutube());
            dto.setUserYoutube(license.getUserYoutube());

            // Herramientas digitales
            dto.setAdobe(license.isAdobe());
            dto.setMailchimp(license.isMailchimp());
            dto.setLinktree(license.isLinktree());

            // E-commerce
            dto.setMagento(license.isMagento());
            dto.setMagentoUser(license.getMagentoUser());
            dto.setShopify(license.isShopify());
            dto.setUserShopify(license.getUserShopify());
            dto.setMercadoLibre(license.isMercadoLibre());
            dto.setAmazon(license.isAmazon());
            dto.setConekta(license.isConekta());
            dto.setOpenPay(license.isOpenPay());
            dto.setKuesky(license.isKuesky());

            // Autenticación extra
            dto.setAuthPhoneNumber(license.getAuthPhoneNumber());
            dto.setAuthTwoFactorAuthenticationName(license.getAuthTwoFactorAuthenticationName());
            dto.setAuthDepartament(license.getAuthDepartament());
            dto.setStatus(license.isStatus());

            return dto;
        }).collect(Collectors.toList());

    }


    public ResponseAccessCardDto getActiveAccessCardByPersonId(Long id) {
        BeanPerson person = personRepository.findById(id)
                .orElseThrow(() -> new CustomException("Empleado no encontrado"));

        BeanAccessCard card = person.getAccessCard();

        if (card != null) {
            return new ResponseAccessCardDto(
                    card.getAccessCardId(),
                    card.isAccessBetweenBuildings(),
                    card.isMainDoor(),
                    card.isAccessTechnicalService(),
                    card.isMainWarehouse(),
                    card.isWarehouseBasement(),
                    card.isTechnicalServiceWarehouses(),
                    card.isTechnicalServiceWarehousesTwo() // Puedes conservar este valor si sigue siendo informativo
            );
        }

        return null; // No tiene tarjeta asignada
    }






}
