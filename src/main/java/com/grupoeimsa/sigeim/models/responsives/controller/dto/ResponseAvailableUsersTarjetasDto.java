package com.grupoeimsa.sigeim.models.responsives.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAvailableUsersTarjetasDto {
    private Long personId;
    private String fullName;
}
