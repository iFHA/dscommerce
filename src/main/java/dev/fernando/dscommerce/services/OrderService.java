package dev.fernando.dscommerce.services;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscommerce.dto.OrderDTO;
import dev.fernando.dscommerce.entities.Order;
import dev.fernando.dscommerce.entities.OrderItem;
import dev.fernando.dscommerce.entities.Product;
import dev.fernando.dscommerce.enums.OrderStatus;
import dev.fernando.dscommerce.repositories.OrderItemRepository;
import dev.fernando.dscommerce.repositories.OrderRepository;
import dev.fernando.dscommerce.repositories.ProductRepository;
import dev.fernando.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {
    private final UserService userService;
    private final OrderRepository repository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;

    public OrderService(final OrderRepository repository,
    final OrderItemRepository orderItemRepository,
    final ProductRepository productRepository, 
    final UserService userService,
    final AuthService authService) {
        this.repository = repository;
        this.userService = userService;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.authService = authService;
    }

    @Transactional
    public OrderDTO store(OrderDTO dto) {
        Order order = new Order();
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        order.setClient(this.userService.authenticated());
        dto.getItems().forEach(itemDTO -> {
            Product product = productRepository.getReferenceById(itemDTO.getProductId());
            OrderItem orderItem = new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
            order.addItem(orderItem);
        });
        Order orderWithId = repository.save(order);
        orderItemRepository.saveAll(order.getItems());
        return new OrderDTO(orderWithId);
    }

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Pedido de id = %d não encontrado!".formatted(id)));
        
        authService.validateSelfOrAdmin(order.getClient().getId());
        
        return new OrderDTO(order);
    }
}
