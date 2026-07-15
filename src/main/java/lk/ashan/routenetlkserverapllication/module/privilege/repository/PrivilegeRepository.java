package lk.ashan.routenetlkserverapllication.module.privilege.repository;

import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {

    List<Privilege> findByRoleId(Integer roleId);

    boolean existsByRoleIdAndModuleIdAndOperationId(
            Integer roleId,
            Integer moduleId,
            Integer operationId
    );

    void deleteByRoleIdAndId(
            Integer roleId,
            Integer privilegeId
    );

    void deleteByRoleId(Integer roleId);

    boolean existsByRoleIdAndId(Integer roleId, Integer privilegeId);
}
