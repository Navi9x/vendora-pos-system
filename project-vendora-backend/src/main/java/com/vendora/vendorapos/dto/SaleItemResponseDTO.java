package com.vendora.vendorapos.dto;


import java.math.BigDecimal;

public record SaleItemResponseDTO(Long id, int quantity, ProductDTO productDTO, BigDecimal unitPrice, BigDecimal totalPrice) {
}
