package com.grupoeimsa.sigeim.models.responsives.controller.dto;

import lombok.Data;

@Data
public class ResponseEditResponsiveCellphoneDto {
    private Long responsiveId;
    private String nombre;
    private String puesto;
    private String entregables;
    private String marca;
    private String color;
    private String numero;
    private String imei;
    private String estado;
    private String fecha;
    private Long cellphoneId;
}
