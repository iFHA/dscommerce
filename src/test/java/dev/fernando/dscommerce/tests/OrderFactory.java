package dev.fernando.dscommerce.tests;

import java.time.Instant;

import dev.fernando.dscommerce.entities.Order;
import dev.fernando.dscommerce.entities.OrderItem;
import dev.fernando.dscommerce.entities.Product;
import dev.fernando.dscommerce.entities.User;
import dev.fernando.dscommerce.enums.OrderStatus;

public class OrderFactory {
    public static Order createClientOrder() {
        return createOrder(
            1L,  
            UserFactory.createClientUser()
        );
    }
    public static Order createAdminOrder() {
        return createOrder(
            2L, 
            UserFactory.createAdminUser()
        );
    }
    public static Order createOrder(Long orderId, User user) {
        Order order = new Order(
            orderId, 
            Instant.now(), 
            OrderStatus.WAITING_PAYMENT, 
            user, 
            null
        );
        Product product = ProductFactory.createProduct();
        OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
        order.addItem(orderItem);
        return order;
    }
}
