package dev.fernando.dscommerce.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.fernando.dscommerce.entities.Role;
import dev.fernando.dscommerce.entities.User;
import dev.fernando.dscommerce.projections.UserDetailsProjection;
import dev.fernando.dscommerce.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> projectionList = repository.searchUserByUsername(username);
        if(projectionList.isEmpty()) {
            throw new UsernameNotFoundException("Usuário %s não encontrado!".formatted(username));
        }
        
        User user = new User();
        UserDetailsProjection projection = projectionList.get(0);
        user.setEmail(username);
        user.setPassword(projection.getPassword());
        projectionList.stream().forEach(p->user.addRole(new Role(p.getRoleId(), p.getAuthority())));

        return user;
    }
}
