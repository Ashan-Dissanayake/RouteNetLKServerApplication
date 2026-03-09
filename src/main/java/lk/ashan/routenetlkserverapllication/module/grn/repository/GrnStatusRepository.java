package lk.ashan.routenetlkserverapllication.module.grn.repository;

import lk.ashan.routenetlkserverapllication.module.grn.model.Grnstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrnStatusRepository extends JpaRepository<Grnstatus, Integer> {
    Optional<Grnstatus> findByName(String pending);
}
