package dev.fernando.dscommerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.fernando.dscommerce.entities.User;
import dev.fernando.dscommerce.services.exceptions.ForbiddenException;
import dev.fernando.dscommerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService service;

    @Mock
    private UserService userService;

    private User admin;
    private User client;

    @BeforeEach
    void setUp() throws Exception {
        admin = UserFactory.createAdminUser();
        client = UserFactory.createClientUser();
    }

    @Test
    void validateSelfOrAdminShouldThrowForbiddenExceptionWhenUserIsNotAdminAndNotOwner() {
        Mockito.when(userService.authenticated()).thenReturn(client);
        Assertions.assertThrows(ForbiddenException.class, () -> service.validateSelfOrAdmin(client.getId() + 1));
    }
    @Test
    void validateSelfOrAdminShouldDoNothingWhenUserIsAdmin() {
        Mockito.when(userService.authenticated()).thenReturn(admin);
        Assertions.assertDoesNotThrow(() -> service.validateSelfOrAdmin(admin.getId()));
    }
    @Test
    void validateSelfOrAdminShouldDoNothingWhenUserIsOwner() {
        Mockito.when(userService.authenticated()).thenReturn(client);
        Assertions.assertDoesNotThrow(() -> service.validateSelfOrAdmin(client.getId()));
    }
}
