package dev.fernando.dscommerce.tests;

import java.time.LocalDate;

import dev.fernando.dscommerce.entities.Role;
import dev.fernando.dscommerce.entities.User;

public class UserFactory {
    public static User createClientUser() {
        var user = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", LocalDate.parse("2001-07-25"), "$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }
    public static User createAdminUser() {
        var user = new User(2L, "Alex Green", "alex@gmail.com", "977777777", LocalDate.parse("1987-12-13"), "$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG");
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }
    public static User createCustomClientUser(Long id, String username) {
        var user = new User(id, "Maria Brown", username, "988888888", LocalDate.parse("2001-07-25"), "$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }
    public static User createCustomAdminUser(Long id, String username) {
        var user = new User(id, "Alex Green", username, "977777777", LocalDate.parse("1987-12-13"), "$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG");
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }
}

