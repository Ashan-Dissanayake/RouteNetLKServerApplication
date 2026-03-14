package lk.ashan.routenetlkserverapllication.module.grn.repository;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrnStatusRepository extends JpaRepository<GrnStatus, Integer> {
    Optional<GrnStatus> findByName(String pending);
}
