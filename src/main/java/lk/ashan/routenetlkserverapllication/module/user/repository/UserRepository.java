package lk.ashan.routenetlkserverapllication.module.user.repository;

import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing `User` entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves the account locked status of a user by their username.
     *
     * @param username the username of the user
     * @return true if the account is locked, false otherwise
     */
    @Query("SELECT u.accountlocked FROM User u WHERE u.username = :username")
    Boolean findUserAccountLockedByUsername(@Param("username") String username);

    /**
     * Checks if a username exists for a user other than the specified user ID.
     *
     * @param username the username to check
     * @param id the ID of the user to exclude from the check
     * @return true if the username exists for another user, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username AND u.id != :id")
    boolean existsByUsernameAndIdNot(@Param("username") String username, @Param("id") Integer id);

    /**
     * Checks if a user exists with the specified username.
     *
     * @param username the username to check
     * @return true if a user exists with the username, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user exists with the specified employee ID.
     *
     * @param id the employee ID to check
     * @return true if a user exists with the employee ID, false otherwise
     */
    boolean existsByEmployee_Id(Integer id);

    /**
     * Finds all users associated with a specific branch ID.
     *
     * @param branchId the branch ID to filter users by
     * @return a list of users associated with the specified branch ID
     */
    List<User> findByEmployee_Branch_Id(Integer branchId);
}
