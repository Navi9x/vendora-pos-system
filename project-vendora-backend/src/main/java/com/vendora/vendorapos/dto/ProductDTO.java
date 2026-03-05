package com.vendora.vendorapos.dto;

public record ProductDTO(
        Long id,
        String name,
        Double price,
        String category,
        String image,
        boolean active
        ) {
}
