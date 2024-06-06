package dev.fernando.dscommerce.entities;

import jakarta.persistence.Entity;

@Entity
public class OrderItem {
    private Integer quantity;
    private Double price;
}
