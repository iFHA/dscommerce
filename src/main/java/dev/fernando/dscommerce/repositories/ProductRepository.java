package dev.fernando.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.fernando.dscommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{}
