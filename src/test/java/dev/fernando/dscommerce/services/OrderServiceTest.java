package dev.fernando.dscommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.fernando.dscommerce.dto.OrderDTO;
import dev.fernando.dscommerce.entities.Order;
import dev.fernando.dscommerce.entities.OrderItem;
import dev.fernando.dscommerce.entities.Product;
import dev.fernando.dscommerce.entities.User;
import dev.fernando.dscommerce.repositories.OrderItemRepository;
import dev.fernando.dscommerce.repositories.OrderRepository;
import dev.fernando.dscommerce.repositories.ProductRepository;
import dev.fernando.dscommerce.services.exceptions.ForbiddenException;
import dev.fernando.dscommerce.services.exceptions.ResourceNotFoundException;
import dev.fernando.dscommerce.tests.OrderFactory;
import dev.fernando.dscommerce.tests.ProductFactory;
import dev.fernando.dscommerce.tests.UserFactory;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private UserService userService;
    @Mock
    private OrderRepository repository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private AuthService authService;

    private Order order;
    private OrderDTO orderDTO;
    private User admin, client;
    private Long existingOrderId, nonExistingOrderId, existingProductId, nonExistingProductId;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existingOrderId = 1L;
        nonExistingOrderId = 2L;
        existingProductId = 1L;
        nonExistingProductId = 2L;

        admin = UserFactory.createAdminUser();
        client = UserFactory.createClientUser();

        order = OrderFactory.createOrder(existingOrderId, client);
        orderDTO = new OrderDTO(order);

        product = ProductFactory.createProduct();

        Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));
        Mockito.when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        Mockito.doThrow(EntityNotFoundException.class).when(productRepository).getReferenceById(nonExistingProductId);

        Mockito.when(repository.save(any())).thenReturn(order);
        Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));

        Mockito.when(userService.authenticated()).thenReturn(admin);
    }

    @Test
    void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
        var result = orderService.findById(existingOrderId);
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(OrderDTO.class, result);
        Assertions.assertEquals(existingOrderId, result.getId());
    }
    @Test
    void findByIdShouldReturnOrderDTOWhenIdExistsAndSelfClientLogged() {
        Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
        var result = orderService.findById(existingOrderId);
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(OrderDTO.class, result);
        Assertions.assertEquals(existingOrderId, result.getId());
    }

    @Test
    void findByIdShouldThrowsForbiddenExceptionWhenIdExistsAndOtherClientLogged() {
        Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(ArgumentMatchers.any());
        Assertions.assertThrows(ForbiddenException.class,
        () -> orderService.findById(existingOrderId));
    }

    @Test
    void findByIdShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class,
        () -> orderService.findById(nonExistingOrderId));
    }

    @Test
    void storeShouldReturnOrderDTOAdminLogged() {
        Mockito.when(userService.authenticated()).thenReturn(admin);
        
        var result = orderService.store(orderDTO);
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(OrderDTO.class, result);
    }
    @Test
    void storeShouldReturnOrderDTOClientLogged() {
        Mockito.when(userService.authenticated()).thenReturn(client);

        var result = orderService.store(orderDTO);
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(OrderDTO.class, result);
    }
    @Test
    void storeShouldThrowsUsernameNotFoundExceptionWhenUserNotLogged() {
        Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();
        order.setClient(new User());
        orderDTO = new OrderDTO(order);
        Assertions.assertThrows(UsernameNotFoundException.class, () -> orderService.store(orderDTO));
    }
    
    @Test
    void storeShouldThrowsEntityNotFoundExceptionWhenProductDoesNotExist() {
        Mockito.when(userService.authenticated()).thenReturn(client);
        product.setId(nonExistingOrderId);
        orderDTO.addItem(new OrderItem(order, product, 5, 15.0));
        Assertions.assertThrows(EntityNotFoundException.class, () -> orderService.store(orderDTO));
    }
    
}
