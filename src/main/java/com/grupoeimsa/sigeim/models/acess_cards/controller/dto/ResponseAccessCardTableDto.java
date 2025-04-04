package com.grupoeimsa.sigeim.models.acess_cards.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAccessCardTableDto {
    private Long accessCardId;
    private Long personId;
    private String fullName;
    private boolean accessBetweenBuildings;
    private boolean mainDoor;
    private boolean accessTechnicalService;
    private boolean mainWarehouse;
    private boolean warehouseBasement;
    private boolean technicalServiceWarehouses;
}
