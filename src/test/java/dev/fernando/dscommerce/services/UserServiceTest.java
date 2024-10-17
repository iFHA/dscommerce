package dev.fernando.dscommerce.services;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.fernando.dscommerce.entities.User;
import dev.fernando.dscommerce.projections.UserDetailsProjection;
import dev.fernando.dscommerce.repositories.UserRepository;
import dev.fernando.dscommerce.tests.UserDetailsProjectionFactory;
import dev.fernando.dscommerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    private String existingUsername;
    private String nonExistingUsername;

    private User user;

    private List<UserDetailsProjection> userDetailsList;

    @BeforeEach
    void setUp() throws Exception {
        existingUsername = "usuarioExistente@gmail.com";
        nonExistingUsername = "usuarioInexistente@gmail.com";
        user = UserFactory.createCustomAdminUser(1L, existingUsername);

        userDetailsList = UserDetailsProjectionFactory.createCustomClientUser(existingUsername);

        Mockito.when(repository.searchUserByUsername(existingUsername)).thenReturn(userDetailsList);
        Mockito.when(repository.searchUserByUsername(nonExistingUsername)).thenReturn(Collections.emptyList());

    }

    @Test
    void loadUserByUsernameShouldReturnUserDetailsWhenUsernameExists() {
        var result = service.loadUserByUsername(existingUsername);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingUsername, result.getUsername());
        Assertions.assertInstanceOf(UserDetails.class, result);
    }

    @Test
    void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUsernameDoesNotExist() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(nonExistingUsername));
    }
}
