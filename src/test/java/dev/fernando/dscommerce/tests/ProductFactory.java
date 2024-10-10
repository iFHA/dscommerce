package dev.fernando.dscommerce.tests;

import dev.fernando.dscommerce.entities.Product;

public class ProductFactory {
    public static Product createProduct() {
        var category = CategoryFactory.createCategory();
        var product = new Product(1L, "Produto teste", "Descrição teste", 99.99, "https://teste.com/img.jpg");
        product.addCategory(category);
        return product;
    }
    public static Product createProduct(String name) {
        var product = createProduct();
        product.setName(name);
        return product;
    }
}
