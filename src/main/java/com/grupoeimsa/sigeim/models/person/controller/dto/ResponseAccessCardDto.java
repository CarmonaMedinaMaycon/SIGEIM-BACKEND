package com.grupoeimsa.sigeim.models.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAccessCardDto {
    private Long accessCardId;
    private boolean accessBetweenBuildings;
    private boolean mainDoor;
    private boolean accessTechnicalService;
    private boolean mainWarehouse;
    private boolean warehouseBasement;
    private boolean technicalServiceWarehouses;
    private boolean technicalServiceWarehousesTwo;
}