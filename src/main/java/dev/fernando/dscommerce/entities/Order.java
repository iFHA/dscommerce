package dev.fernando.dscommerce.entities;

import java.time.Instant;

import dev.fernando.dscommerce.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant moment;
    private OrderStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private User client;
}
