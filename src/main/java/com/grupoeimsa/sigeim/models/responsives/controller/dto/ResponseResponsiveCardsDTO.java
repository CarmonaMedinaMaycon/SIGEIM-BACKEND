package com.grupoeimsa.sigeim.models.responsives.controller.dto;

import com.grupoeimsa.sigeim.models.responsives.model.EStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResponsiveCardsDTO {
    private Long id;
    private String fechaCreacion;
    private String nombreEmpleado;
    private String estado;
    private boolean hasSignedDocument;
}