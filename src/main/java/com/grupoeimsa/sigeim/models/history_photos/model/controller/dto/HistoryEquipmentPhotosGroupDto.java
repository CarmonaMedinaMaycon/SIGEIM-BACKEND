package com.grupoeimsa.sigeim.models.history_photos.model.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class HistoryEquipmentPhotosGroupDto {
    private LocalDate date;
    private String personName;
    private List<String> photos;
}
