package lk.ashan.routenetlkserverapllication.module.user.repository;

import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Role entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Finds a Role entity by its name.
     *
     * @param driver the name of the role to search for
     * @return an Optional containing the Role entity if found, or empty if not found
     */
    Optional<Role> findByName(String driver);
}
