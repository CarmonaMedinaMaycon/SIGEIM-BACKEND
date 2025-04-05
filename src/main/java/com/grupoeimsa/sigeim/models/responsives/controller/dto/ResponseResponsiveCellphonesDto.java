package com.grupoeimsa.sigeim.models.responsives.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseResponsiveCellphonesDto {
    private Long id;
    private String fechaCreacion;
    private String nombreEmpleado;
    private String imei;
    private String estado;
    private boolean hasSignedDocument;
}
