package dev.fernando.dscommerce.dto;

import dev.fernando.dscommerce.entities.Category;

public record CategoryDTO(Long id, String name) {
    public CategoryDTO(Category entity) {
        this(entity.getId(), entity.getName());
    }
}
