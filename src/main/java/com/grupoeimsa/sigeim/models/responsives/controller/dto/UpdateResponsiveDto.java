package com.grupoeimsa.sigeim.models.responsives.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResponsiveDto {
    private Long responsiveId;
    private String templateName;
    private Map<String, String> placeholders;
    private List<Map<String, String>> equipaments;
}
