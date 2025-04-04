package com.grupoeimsa.sigeim.models.acess_cards.service;

import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.ResponseAccessCardDTO;
import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.ResponseAccessCardTableDto;
import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.ResponseRegisterAccessCardDTO;
import com.grupoeimsa.sigeim.models.acess_cards.model.BeanAccessCard;
import com.grupoeimsa.sigeim.models.acess_cards.model.IAcessCard;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

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
        BeanAccessCard accessCard = new BeanAccessCard();

        accessCard.setAccessTechnicalService(dto.isAccessTechnicalService());
        accessCard.setAccessBetweenBuildings(dto.isAccessBetweenBuildings());
        accessCard.setMainDoor(dto.isMainDoor());
        accessCard.setMainWarehouse(dto.isMainWarehouse());
        accessCard.setTechnicalServiceWarehouses(dto.isTechnicalServiceWarehouses());
        accessCard.setWarehouseBasement(dto.isWarehouseBasement());

        // Buscar a la persona por ID y asignarla
        BeanPerson person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new CustomException("El usuario no fue encontrado"));

        accessCard.setPerson(person);

        accessCardRepository.save(accessCard);
    }

    @Transactional
    public void update(ResponseRegisterAccessCardDTO dto) {
        BeanAccessCard accessCard = accessCardRepository.findById(dto.getAccessCardId())
                .orElseThrow(() -> new CustomException("Access card not found"));

        accessCard.setAccessTechnicalService(dto.isAccessTechnicalService());
        accessCard.setAccessBetweenBuildings(dto.isAccessBetweenBuildings());
        accessCard.setMainDoor(dto.isMainDoor());
        accessCard.setMainWarehouse(dto.isMainWarehouse());
        accessCard.setWarehouseBasement(dto.isWarehouseBasement());
        accessCard.setTechnicalServiceWarehouses(dto.isTechnicalServiceWarehouses());

        // 🔁 Asignar la persona por ID
        BeanPerson person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new CustomException("El usuario no fue encontrado"));

        accessCard.setPerson(person);

        accessCardRepository.save(accessCard);
    }

    @Transactional(readOnly = true)
    public Page<ResponseAccessCardTableDto> getAccessCardSummaries(String search, int page, int size, Boolean status, String enterprise, String departament) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BeanAccessCard> accessCards = accessCardRepository.findAllByPersonName(
                search,
                departament,
                enterprise,
                status,
                pageable
        );

        if (accessCards.isEmpty()) {
            throw new CustomException("No access cards found");
        }

        return accessCards.map(card -> new ResponseAccessCardTableDto(
                card.getAccessCardId(),
                card.getPerson().getPersonId(),
                card.getPerson().getFullName(),
                card.isAccessBetweenBuildings(),
                card.isMainDoor(),
                card.isAccessTechnicalService(),
                card.isMainWarehouse(),
                card.isWarehouseBasement(),
                card.isTechnicalServiceWarehouses()
        ));
    }


    @Transactional
    public void delete(Long id) {
        accessCardRepository.deleteById(id);
    }

}
