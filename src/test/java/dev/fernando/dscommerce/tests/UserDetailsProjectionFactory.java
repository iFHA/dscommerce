package dev.fernando.dscommerce.tests;

import java.util.ArrayList;
import java.util.List;

import dev.fernando.dscommerce.projections.UserDetailsProjection;

public class UserDetailsProjectionFactory {
    public static List<UserDetailsProjection> createCustomClientUser(String username) {
        List<UserDetailsProjection> list = new ArrayList<>();
        list.add(new UserDetailsProjectionImpl(username, "123", 1L, "ROLE_CLIENT"));
        return list;
    }
    public static List<UserDetailsProjection> createCustomAdminUser(String username) {
        List<UserDetailsProjection> list = new ArrayList<>();
        list.add(new UserDetailsProjectionImpl(username, "123", 2L, "ROLE_ADMIN"));
        return list;
    }
    public static List<UserDetailsProjection> createCustomAdminClientUser(String username) {
        List<UserDetailsProjection> list = new ArrayList<>();
        list.add(new UserDetailsProjectionImpl(username, "123", 1L, "ROLE_CLIENT"));
        list.add(new UserDetailsProjectionImpl(username, "123", 2L, "ROLE_ADMIN"));
        return list;
    }
}

class UserDetailsProjectionImpl implements UserDetailsProjection {
    private String username;
    private String password;
    private Long roleId;
    private String authority;
    public UserDetailsProjectionImpl(
        String username,
        String password,
        Long roleId,
        String authority
    ) {
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.authority = authority;
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Long getRoleId() {
        return roleId;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
