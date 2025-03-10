package com.grupoeimsa.sigeim.models.acess_cards.service;

import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.ResponseAccessCardDTO;
import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.ResponseRegisterAccessCardDTO;
import com.grupoeimsa.sigeim.models.acess_cards.model.BeanAccessCard;
import com.grupoeimsa.sigeim.models.acess_cards.model.IAcessCard;
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


    public AccessCardService(IAcessCard accessCardRepository) {
        this.accessCardRepository = accessCardRepository;
    }

    @Transactional(readOnly = true)
    public Page<ResponseAccessCardDTO> findAll (String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BeanAccessCard> accessCards = accessCardRepository.findAllByPersonName(
                search,
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
    public void registerAccessCard(ResponseRegisterAccessCardDTO responseRegisterAccessCardDTO){
        BeanAccessCard accessCard = new BeanAccessCard();
        accessCard.setAccessTechnicalService(responseRegisterAccessCardDTO.isAccessTechnicalService());
        accessCard.setAccessBetweenBuildings(responseRegisterAccessCardDTO.isAccessBetweenBuildings());
        accessCard.setMainDoor(responseRegisterAccessCardDTO.isMainDoor());
        accessCard.setMainWarehouse(responseRegisterAccessCardDTO.isMainWarehouse());
        accessCard.setTechnicalServiceWarehouses(responseRegisterAccessCardDTO.isTechnicalServiceWarehouses());
        accessCard.setWarehouseBasement(responseRegisterAccessCardDTO.isWarehouseBasement());
        accessCard.setPerson(responseRegisterAccessCardDTO.getPerson());
        accessCardRepository.save(accessCard);
    }

    @Transactional
    public void update(ResponseRegisterAccessCardDTO responseRegisterAccessCardDTO){
        BeanAccessCard accessCard = accessCardRepository.findById(responseRegisterAccessCardDTO.getAccessCardId()).orElseThrow(() -> new CustomException("Person not found"));
        accessCard.setAccessTechnicalService(responseRegisterAccessCardDTO.isAccessTechnicalService());
        accessCard.setAccessBetweenBuildings(responseRegisterAccessCardDTO.isAccessBetweenBuildings());
        accessCard.setMainDoor(responseRegisterAccessCardDTO.isMainDoor());
        accessCard.setMainWarehouse(responseRegisterAccessCardDTO.isMainWarehouse());
        accessCard.setWarehouseBasement(responseRegisterAccessCardDTO.isWarehouseBasement());
        accessCard.setTechnicalServiceWarehouses(responseRegisterAccessCardDTO.isTechnicalServiceWarehouses());
        accessCard.setPerson(responseRegisterAccessCardDTO.getPerson());
        accessCardRepository.save(accessCard);

    }

}
