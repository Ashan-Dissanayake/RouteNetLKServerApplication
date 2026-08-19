package lk.ashan.routenetlkserverapllication.module.user.repository;

import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `UserType` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {

    /**
     * Finds a `UserType` entity by its name.
     *
     * @param name the name of the `UserType` to find
     * @return an `Optional` containing the `UserType` if found, or empty if not found
     */
    Optional<UserType> findByName(String name);
}
