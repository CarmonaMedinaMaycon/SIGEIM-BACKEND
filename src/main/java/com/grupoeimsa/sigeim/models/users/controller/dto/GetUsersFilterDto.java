package com.grupoeimsa.sigeim.models.users.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUsersFilterDto {
    private String status;
    private String search;
    private int page;
    private int size;
}
