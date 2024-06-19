package dev.fernando.dscommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.fernando.dscommerce.entities.User;
import dev.fernando.dscommerce.projections.UserDetailsProjection;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(
        nativeQuery = false,
        value = "SELECT " +
        "a.email as username, a.password, b.role_id as roleId, c.authority " +
        "FROM tb_user a " +
        "JOIN tb_user_role b on b.user_id=a.id " +
        "JOIN tb_role c ON c.id=b.role_id" +
        "WHERE a.email = :email"
    )
    List<UserDetailsProjection> searchUserByUsername(String email);
}
