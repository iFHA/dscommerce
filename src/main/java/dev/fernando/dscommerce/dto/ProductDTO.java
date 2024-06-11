package dev.fernando.dscommerce.dto;

import dev.fernando.dscommerce.entities.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductDTO(
    Long id,
    @NotBlank(message = "Campo \"nome\" é obrigatório")
    @Size(min = 3, max = 80, message = "Nome precisa ter de 3 a 80 caracteres")
    String name,
    @Size(min = 10, message = "Descrição precisa ter no mínimo 10 caracteres")
    String description,
    @Positive(message = "O preço deve ser positivo")
    Double price,
    String imgUrl
) {
    public ProductDTO(Product entity) {
        this(entity.getId(),
        entity.getName(),
        entity.getDescription(),
        entity.getPrice(),
        entity.getImgUrl());
    }
}
