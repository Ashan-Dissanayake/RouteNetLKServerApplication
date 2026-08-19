package lk.ashan.routenetlkserverapllication.module.user.repository;

import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing `UserRole` entities.
 * Provides methods for retrieving, checking existence, and deleting user roles.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    /**
     * Finds all user roles associated with a specific user ID.
     *
     * @param userId the ID of the user whose roles are to be retrieved
     * @return a list of `UserRole` entities associated with the given user ID
     */
    List<UserRole> findByUserId(Integer userId);

    /**
     * Checks if a specific role exists for a given user.
     *
     * @param userId the ID of the user
     * @param roleId the ID of the role
     * @return `true` if the role exists for the user, `false` otherwise
     */
    boolean existsByUserIdAndRoleId(Integer userId, Integer roleId);

    /**
     * Deletes a specific role associated with a given user.
     *
     * @param userId the ID of the user
     * @param roleId the ID of the role to be deleted
     */
    void deleteByUserIdAndRoleId(Integer userId, Integer roleId);

    /**
     * Deletes all roles associated with a specific user.
     *
     * @param userId the ID of the user whose roles are to be deleted
     */
    void deleteByUserId(Integer userId);
}
