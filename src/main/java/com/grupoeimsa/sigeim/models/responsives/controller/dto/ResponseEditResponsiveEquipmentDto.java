package com.grupoeimsa.sigeim.models.responsives.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEditResponsiveEquipmentDto {
    private String responsibleName;
    private String responsibleDepartament;
    private String responsiblePosition;
    private String branch;
    private String description;
    private String observations;
    private String whoGives;
    private List<Map<String, String>> equipaments; // Cada equipo representado como un Map con claves tipo "Tipo", "Marca", etc.
}