package lk.ashan.routenetlkserverapllication.module.privilege.repository;

import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Privilege entities.
 * Provides methods for querying and manipulating privilege data.
 */
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {

    /**
     * Finds a list of privileges by the given role ID.
     *
     * @param roleId the ID of the role
     * @return a list of privileges associated with the specified role ID
     */
    List<Privilege> findByRoleId(Integer roleId);

    /**
     * Checks if a privilege exists for the given role ID, module ID, and operation ID.
     *
     * @param roleId the ID of the role
     * @param moduleId the ID of the module
     * @param operationId the ID of the operation
     * @return true if a privilege exists, false otherwise
     */
    boolean existsByRoleIdAndModuleIdAndOperationId(
            Integer roleId,
            Integer moduleId,
            Integer operationId
    );

    /**
     * Deletes a privilege by the given role ID and privilege ID.
     *
     * @param roleId the ID of the role
     * @param privilegeId the ID of the privilege
     */
    void deleteByRoleIdAndId(
            Integer roleId,
            Integer privilegeId
    );

    /**
     * Checks if a privilege exists for the given role ID and privilege ID.
     *
     * @param roleId the ID of the role
     * @param privilegeId the ID of the privilege
     * @return true if a privilege exists, false otherwise
     */
    boolean existsByRoleIdAndId(Integer roleId, Integer privilegeId);
}
