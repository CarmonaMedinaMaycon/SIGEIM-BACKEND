package com.grupoeimsa.sigeim.models.cellphones.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailablePersonCellphoneDto {
    private Long personId;
    private String fullName;
    private String departament;
    private Boolean hasCellphone;
}
