package com.grupoeimsa.sigeim.models.responsives.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestSearchResponsiveEquipmentsDto {
    private String search;
    private String estado; // "Activa y firmada", "Activa por firmar", "Cancelada"
    private String sort;   // "asc" o "desc"
    private int page = 0;
    private int size = 10;
}