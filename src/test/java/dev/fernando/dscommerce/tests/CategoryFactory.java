package dev.fernando.dscommerce.tests;

import dev.fernando.dscommerce.entities.Category;

public class CategoryFactory {
    public static Category createCategory() {
        return new Category(1L, "Categoria Teste");
    }
    public static Category createCategory(Long id, String name) {
        return new Category(id, name);
    }
}
