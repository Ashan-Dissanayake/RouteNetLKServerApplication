package lk.ashan.routenetlkserverapllication.module.user.repository;

import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    List<UserRole> findByUserId(Integer userId);

    boolean existsByUserIdAndRoleId(Integer userId, Integer roleId);

    void deleteByUserIdAndRoleId(Integer userId, Integer roleId);

    void deleteByUserId(Integer userId);
}
