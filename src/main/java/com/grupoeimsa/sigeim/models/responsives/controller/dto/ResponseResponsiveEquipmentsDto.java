package com.grupoeimsa.sigeim.models.responsives.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseResponsiveEquipmentsDto {
    private Long id;
    private String fechaCreacion;
    private String nombreEmpleado;
    private String numeroSerie;
    private String estado;
    private Boolean hasSignedDocument;
}