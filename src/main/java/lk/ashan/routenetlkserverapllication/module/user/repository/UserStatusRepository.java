package lk.ashan.routenetlkserverapllication.module.user.repository;

import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing `UserStatus` entities.
 * Extends `JpaRepository` to provide CRUD operations and custom query methods.
 */
@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {

    /**
     * Finds a `UserStatus` entity by its name.
     *
     * @param name the name of the `UserStatus` to find
     * @return an `Optional` containing the `UserStatus` if found, or empty if not found
     */
    Optional<UserStatus> findByName(String name);
}
