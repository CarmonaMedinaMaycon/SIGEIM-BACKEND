package com.grupoeimsa.sigeim.models.acess_cards.controller.dto;


import com.grupoeimsa.sigeim.models.acess_cards.model.BeanAccessCard;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseRegisterAccessCardDTO {

    private Long accessCardId;
    private boolean accessBetweenBuildings;
    private boolean mainDoor;
    private boolean accessTechnicalService;
    private boolean mainWarehouse;
    private boolean WarehouseBasement;
    private boolean TechnicalServiceWarehouses;
    private BeanPerson person;

    public ResponseRegisterAccessCardDTO(BeanAccessCard beanAccessCard) {
        this.accessCardId = beanAccessCard.getAccessCardId();
        this.accessBetweenBuildings = beanAccessCard.isAccessBetweenBuildings();
        this.mainDoor = beanAccessCard.isMainDoor();
        this.accessTechnicalService = beanAccessCard.isAccessTechnicalService();
        this.mainWarehouse = beanAccessCard.isMainWarehouse();
        this.WarehouseBasement = beanAccessCard.isWarehouseBasement();
        this.TechnicalServiceWarehouses = beanAccessCard.isTechnicalServiceWarehouses();
        this.person = beanAccessCard.getPerson();
    }
}
