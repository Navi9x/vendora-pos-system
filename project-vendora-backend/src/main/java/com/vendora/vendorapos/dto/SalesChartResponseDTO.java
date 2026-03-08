package com.vendora.vendorapos.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesChartResponseDTO {
    private String label;
    private BigDecimal revenue;
    private Long transactions;
}
