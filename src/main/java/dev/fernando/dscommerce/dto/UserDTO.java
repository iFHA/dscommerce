package dev.fernando.dscommerce.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import dev.fernando.dscommerce.entities.User;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private List<String> roles = new ArrayList<>();

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.birthDate = entity.getBirthDate();
        fillRolesFromCollection(entity.getRoles());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public List<String> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    private void fillRolesFromCollection(Collection<? extends GrantedAuthority> roles) {
        roles.stream().forEach(this::addRole);
    }

    private void addRole(GrantedAuthority role) {
        this.roles.add(role.getAuthority());
    }
    
}
