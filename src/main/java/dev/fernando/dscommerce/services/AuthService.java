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

    public void validateSelfOrAdmin(Long userId) {
        User me = userService.authenticated();
        boolean isAdmin = me.hasRole("ROLE_ADMIN");
        if(isAdmin){
            return;
        }
        boolean isOwner = me.getId().equals(userId);
        if(!isOwner) {
            throw new ForbiddenException("Você não é ADMIN ou não é o dono desse recurso");
        }
    }
}
