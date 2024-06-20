package dev.fernando.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.fernando.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
