package dev.fernando.dscommerce.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscommerce.dto.UserDTO;
import dev.fernando.dscommerce.entities.Role;
import dev.fernando.dscommerce.entities.User;
import dev.fernando.dscommerce.projections.UserDetailsProjection;
import dev.fernando.dscommerce.repositories.UserRepository;
import dev.fernando.dscommerce.util.CustomUserUtil;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final CustomUserUtil util;

    public UserService(
        UserRepository repository,
        CustomUserUtil util
    ) {
        this.repository = repository;
        this.util = util;
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

    protected User authenticated() {
        try {
            return repository.findByEmail(util.getLoggedUsername()).get();
        } catch(Exception e) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
    }

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        return new UserDTO(authenticated());
    }
}
