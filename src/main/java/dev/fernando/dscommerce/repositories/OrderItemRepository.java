package dev.fernando.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.fernando.dscommerce.entities.OrderItem;
import dev.fernando.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
}
