package com.grupoeimsa.sigeim.models.history_photos.model.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoHistoryGroupDto {
    private String personName;
    private LocalDate date;
    private List<String> photos;
}
