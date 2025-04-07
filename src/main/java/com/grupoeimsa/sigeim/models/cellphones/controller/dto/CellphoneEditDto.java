package com.grupoeimsa.sigeim.models.cellphones.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellphoneEditDto {
    private Long cellphoneId;
    private String equipamentName;     // Nombre del equipo
    private String legalName;          // Razón social
    private String company;            // Compañía telefónica
    private int shortDialing;          // Marcación corta
    private String imei;
    private Boolean whatsappBussiness;
    private LocalDate dateRenovation;
    private String comments;
    private Long personId;
}
