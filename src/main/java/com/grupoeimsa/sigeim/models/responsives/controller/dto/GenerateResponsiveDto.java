package com.grupoeimsa.sigeim.models.responsives.controller.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GenerateResponsiveDto {
    private Long templateId;
    private Map<String, String> placeholders;
    private List<Map<String, String>> equipaments;
    private Long equipmentId;
}
