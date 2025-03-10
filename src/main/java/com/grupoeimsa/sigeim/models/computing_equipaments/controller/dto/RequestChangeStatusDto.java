package com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto;

import com.grupoeimsa.sigeim.models.computing_equipaments.model.CEStatus;
import lombok.Data;

@Data
public class RequestChangeStatusDto {
    private Long equipmentId;
    private CEStatus newStatus;
}
