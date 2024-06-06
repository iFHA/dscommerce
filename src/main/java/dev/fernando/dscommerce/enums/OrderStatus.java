package dev.fernando.dscommerce.enums;

public enum OrderStatus {
    
    WAITING_PAYMENT(0),
    PAID(1),
    SHIPPED(2),
    DELIVERED(3),
    CANCELED(4);

    private int value;

    private OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
