package com.grupoeimsa.sigeim.models.acess_cards.service;

import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.ResponseAccessCardDTO;
import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.ResponseAccessCardTableDto;
import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.ResponseRegisterAccessCardDTO;
import com.grupoeimsa.sigeim.models.acess_cards.model.BeanAccessCard;
import com.grupoeimsa.sigeim.models.acess_cards.model.IAcessCard;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.models.responsives.model.EStatus;
import com.grupoeimsa.sigeim.utils.CustomException;
import jakarta.persistence.criteria.Join;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class AccessCardService {

    public final IAcessCard accessCardRepository;
    public final IPerson personRepository;


    public AccessCardService(IAcessCard accessCardRepository, IPerson personRepository) {
        this.accessCardRepository = accessCardRepository;
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public Page<ResponseAccessCardDTO> findAll (String search, int page, int size,  Boolean status, String enterprise, String departament) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BeanAccessCard> accessCards = accessCardRepository.findAllByPersonName(
                search,
                departament,
                enterprise,
                status,
                pageable
        );
        if (accessCards.isEmpty()){
            throw new CustomException("No persons were found");
        }
        return accessCards.map(ResponseAccessCardDTO::new);
    }

    @Transactional(readOnly = true)
    public ResponseAccessCardDTO findById (Long id) {
        BeanAccessCard accessCard = accessCardRepository.findById(id).orElseThrow(() -> new CustomException("The user was not found"));
        return new ResponseAccessCardDTO(accessCard);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void registerAccessCard(ResponseRegisterAccessCardDTO dto) {
        BeanPerson person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new CustomException("El usuario no fue encontrado"));

        // Validar que no tenga ya una tarjeta asignada (sin importar status)
        if (person.getAccessCard() != null) {
            throw new CustomException("El usuario ya tiene una tarjeta de acceso registrada.");
        }

        BeanAccessCard accessCard = new BeanAccessCard();
        accessCard.setAccessBetweenBuildings(dto.isAccessBetweenBuildings());
        accessCard.setMainDoor(dto.isMainDoor());
        accessCard.setAccessTechnicalService(dto.isAccessTechnicalService());
        accessCard.setMainWarehouse(dto.isMainWarehouse());
        accessCard.setWarehouseBasement(dto.isWarehouseBasement());
        accessCard.setTechnicalServiceWarehouses(dto.isTechnicalServiceWarehouses());
        accessCard.setTechnicalServiceWarehousesTwo(dto.isTechnicalServiceWarehousesTwo());
        accessCard.setAccessCardNumber(dto.getAccessCardNumber());
        accessCard.setPerson(person);

        accessCardRepository.save(accessCard);
    }



    @Transactional
    public void update(ResponseRegisterAccessCardDTO dto) {
        BeanAccessCard accessCard = accessCardRepository.findById(dto.getAccessCardId())
                .orElseThrow(() -> new CustomException("Access card not found"));

        accessCard.setAccessBetweenBuildings(dto.isAccessBetweenBuildings());
        accessCard.setMainDoor(dto.isMainDoor());
        accessCard.setAccessTechnicalService(dto.isAccessTechnicalService());
        accessCard.setMainWarehouse(dto.isMainWarehouse());
        accessCard.setWarehouseBasement(dto.isWarehouseBasement());
        accessCard.setTechnicalServiceWarehouses(dto.isTechnicalServiceWarehouses());
        accessCard.setTechnicalServiceWarehousesTwo(dto.isTechnicalServiceWarehousesTwo());
        accessCard.setAccessCardNumber(dto.getAccessCardNumber());
        // 🔁 Asignar la persona por ID
        BeanPerson person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new CustomException("El usuario no fue encontrado"));

        accessCard.setPerson(person);

        accessCardRepository.save(accessCard);
    }

    @Transactional(readOnly = true)
    public Page<ResponseAccessCardTableDto> getAccessCardSummaries(
            String search,
            int page,
            int size,
            Boolean status,
            String enterprise,
            String departament) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<BeanAccessCard> spec = (root, query, cb) -> {
            Join<Object, Object> personJoin = root.join("person");

            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(personJoin.get("name")), searchPattern),
                        cb.like(cb.lower(personJoin.get("lastname")), searchPattern),
                        cb.like(cb.lower(personJoin.get("surname")), searchPattern)
                ));
            }

            if (enterprise != null && !enterprise.isBlank()) {
                predicates.add(cb.equal(cb.lower(personJoin.get("enterprise")), enterprise.toLowerCase()));
            }

            if (departament != null && !departament.isBlank()) {
                predicates.add(cb.equal(cb.lower(personJoin.get("departament")), departament.toLowerCase()));
            }

            if (status != null) {
                predicates.add(cb.equal(personJoin.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<BeanAccessCard> accessCards = accessCardRepository.findAll(spec, pageable);

        return accessCards.map(card -> new ResponseAccessCardTableDto(
                card.getAccessCardId(),
                card.getPerson().getPersonId(),
                card.getPerson().getFullName(),
                card.isAccessBetweenBuildings(),
                card.isMainDoor(),
                card.isAccessTechnicalService(),
                card.isMainWarehouse(),
                card.isWarehouseBasement(),
                card.isTechnicalServiceWarehouses(),
                card.isTechnicalServiceWarehousesTwo(),
                card.getAccessCardNumber()
        ));
    }


    @Transactional
    public void delete(Long id) {
        BeanAccessCard accessCard = accessCardRepository.findById(id)
                .orElseThrow(() -> new CustomException("Tarjeta de acceso no encontrada"));

        // 1. Cancelar responsivas activas
        if (accessCard.getResponsives() != null && !accessCard.getResponsives().isEmpty()) {
            accessCard.getResponsives().forEach(responsive -> {
                if (responsive.getStatus() == EStatus.ACTIVA_FIRMADA || responsive.getStatus() == EStatus.ACTIVA_POR_FIRMAR) {
                    responsive.setStatus(EStatus.CANCELADA);
                }
            });
        }

        // 2. Eliminar la tarjeta del sistema
        accessCardRepository.delete(accessCard);
    }



}
