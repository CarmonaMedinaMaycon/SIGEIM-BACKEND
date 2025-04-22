package com.grupoeimsa.sigeim.models.invoices.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUnassignedEquipmentDto {
    private Long computerEquipamentId;
    private String brand;
    private String serialNumber;
}
