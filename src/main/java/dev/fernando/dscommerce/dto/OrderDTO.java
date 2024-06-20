package dev.fernando.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dev.fernando.dscommerce.entities.Order;
import dev.fernando.dscommerce.entities.OrderItem;
import dev.fernando.dscommerce.enums.OrderStatus;
import jakarta.validation.constraints.NotEmpty;

public class OrderDTO {
    private Long id;
    private Instant moment;
    private OrderStatus status;
    private ClientDTO client;
    private PaymentDTO payment;

    @NotEmpty(message = "Pedido deve ter pelo menos um item")
    private List<OrderItemDTO> items = new ArrayList<>();
    
    public OrderDTO(Long id, Instant moment, OrderStatus status, ClientDTO client, PaymentDTO payment) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.client = client;
        this.payment = payment;
    }
    
    public OrderDTO(Order entity) {
        this.id = entity.getId();
        this.moment = entity.getMoment();
        this.status = entity.getStatus();
        this.client = new ClientDTO(entity.getClient());
        if(!Objects.isNull(entity.getPayment())) {
            this.payment = new PaymentDTO(entity.getPayment());
        }
        entity.getItems().forEach(this::addItem);
    }

    public Long getId() {
        return id;
    }

    public Instant getMoment() {
        return moment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public ClientDTO getClient() {
        return client;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public List<OrderItemDTO> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(OrderItem entity) {
        this.items.add(new OrderItemDTO(entity));
    }

    public Double getTotal() {
        if(items.isEmpty()) {
            return 0.0;
        }
        return items.stream()
        .map(OrderItemDTO::getSubTotal)
        .reduce((subtotal, itemSubtotal) -> subtotal + itemSubtotal)
        .get();
    }
}
