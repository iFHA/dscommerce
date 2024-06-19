package dev.fernando.dscommerce.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.fernando.dscommerce.dto.UserDTO;
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

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaimAsString("username");
            return repository.findByEmail(username).get();
        } catch(Exception e) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
    }

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        return new UserDTO(authenticated());
    }
}
