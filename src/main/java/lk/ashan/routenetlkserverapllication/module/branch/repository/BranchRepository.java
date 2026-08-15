package lk.ashan.routenetlkserverapllication.module.branch.repository;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository interface for managing Branch entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {

    /**
     * Checks if a branch exists with the given code (case-insensitive).
     *
     * @param code the branch code to check
     * @return true if a branch with the given code exists, false otherwise
     */
    boolean existsByCodeEqualsIgnoreCase(String code);

    /**
     * Checks if a branch exists with the given name (case-insensitive).
     *
     * @param name the branch name to check
     * @return true if a branch with the given name exists, false otherwise
     */
    boolean existsByNameEqualsIgnoreCase(String name);

    /**
     * Checks if a branch exists with the given name (case-insensitive) and a different ID.
     *
     * @param name the branch name to check
     * @param id the ID to exclude from the check
     * @return true if a branch with the given name exists and has a different ID, false otherwise
     */
    boolean existsByNameEqualsIgnoreCaseAndIdNot(String name, Integer id);

    /**
     * Checks if a branch exists with the given email (case-insensitive).
     *
     * @param email the branch email to check
     * @return true if a branch with the given email exists, false otherwise
     */
    boolean existsByEmailEqualsIgnoreCase(String email);

    /**
     * Checks if a branch exists with the given email (case-insensitive) and a different ID.
     *
     * @param email the branch email to check
     * @param id the ID to exclude from the check
     * @return true if a branch with the given email exists and has a different ID, false otherwise
     */
    boolean existsByEmailEqualsIgnoreCaseAndIdNot(String email, Integer id);

    /**
     * Checks if a branch exists with the given telephone number.
     *
     * @param telephone the branch telephone number to check
     * @return true if a branch with the given telephone number exists, false otherwise
     */
    boolean existsByTelephone(String telephone);

    /**
     * Checks if a branch exists with the given telephone number and a different ID.
     *
     * @param telephone the branch telephone number to check
     * @param id the ID to exclude from the check
     * @return true if a branch with the given telephone number exists and has a different ID, false otherwise
     */
    boolean existsByTelephoneAndIdNot(String telephone, Integer id);

    /**
     * Checks if a branch exists with the given address (case-insensitive).
     *
     * @param address the branch address to check
     * @return true if a branch with the given address exists, false otherwise
     */
    boolean existsByAddressEqualsIgnoreCase(String address);

    /**
     * Checks if a branch exists with the given address (case-insensitive) and a different ID.
     *
     * @param address the branch address to check
     * @param id the ID to exclude from the check
     * @return true if a branch with the given address exists and has a different ID, false otherwise
     */
    boolean existsByAddressEqualsIgnoreCaseAndIdNot(String address, Integer id);

    /**
     * Marks branches as deleted by setting the deleted flag to true for the given IDs.
     *
     * @param ids the list of branch IDs to mark as deleted
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Branch b SET b.deleted = true WHERE b.id IN :ids")
    void removeAll(@Param("ids") List<Integer> ids);

}
