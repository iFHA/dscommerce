package dev.fernando.dscommerce.dto;

import dev.fernando.dscommerce.entities.Product;

public record ProductMinDTO(
    Long id,
    String name,
    Double price,
    String imgUrl
) {
    public ProductMinDTO(Product entity) {
        this(entity.getId(),
        entity.getName(),
        entity.getPrice(),
        entity.getImgUrl());
    }
}
