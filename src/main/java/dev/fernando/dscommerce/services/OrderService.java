package dev.fernando.dscommerce.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscommerce.dto.OrderDTO;
import dev.fernando.dscommerce.repositories.OrderRepository;
import dev.fernando.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {
    private final OrderRepository repository;

    public OrderService(final OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        return new OrderDTO(
            repository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Pedido de id = %d não encontrado!".formatted(id)))
        );
    }
}
