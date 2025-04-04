package com.grupoeimsa.sigeim.models.acess_cards.controller.dto;

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
    private Long personId;
}
