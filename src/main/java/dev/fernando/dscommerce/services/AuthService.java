package dev.fernando.dscommerce.services;

import org.springframework.stereotype.Service;

import dev.fernando.dscommerce.entities.User;
import dev.fernando.dscommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {

    private final UserService userService;
    public AuthService(final UserService userService) {
        this.userService = userService;
    }

    public void validateSelfOrAdmin(long userId) {
        User me = userService.authenticated();
        boolean isAdmin = me.hasRole("ROLE_ADMIN");
        if (!isAdmin && !me.getId().equals(userId)) {
            throw new ForbiddenException("Você não é ADMIN ou não é o dono desse recurso");
        }
    }
}
