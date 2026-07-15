package lk.ashan.routenetlkserverapllication.module.user.repository;

import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {
    Optional<UserStatus> findByName(String name);
}
