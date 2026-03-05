package com.vendora.vendorapos.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedResponseDTO {
    private List<SaleResponseDTO> items;
    private Integer totalItems;
}
