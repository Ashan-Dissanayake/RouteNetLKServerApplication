package lk.ashan.routenetlkserverapllication.module.permit.repository;

import lk.ashan.routenetlkserverapllication.module.permit.model.Permitestatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermitStatusRepository extends JpaRepository<Permitestatus, Integer> {
    Optional<Permitestatus> findByName(String active);
}
