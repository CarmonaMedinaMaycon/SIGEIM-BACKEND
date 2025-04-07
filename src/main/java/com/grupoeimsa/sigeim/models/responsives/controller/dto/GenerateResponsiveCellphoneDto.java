package com.grupoeimsa.sigeim.models.responsives.controller.dto;

import lombok.Data;

import java.util.Map;

@Data
public class GenerateResponsiveCellphoneDto {
    private String templateName;
    private Map<String, String> placeholders;
    private Long cellphoneId;
}
