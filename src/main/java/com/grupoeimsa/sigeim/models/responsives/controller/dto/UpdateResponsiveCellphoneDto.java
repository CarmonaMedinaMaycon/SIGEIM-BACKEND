package com.grupoeimsa.sigeim.models.responsives.controller.dto;

import lombok.Data;

import java.util.Map;

@Data
public class UpdateResponsiveCellphoneDto {
    private Long responsiveId;
    private String templateName;
    private Map<String, String> placeholders;
    private Long cellphoneId;
}