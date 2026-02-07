package lk.ashan.routenetlkserverapllication.module.permit.repository;

import lk.ashan.routenetlkserverapllication.module.permit.model.Permitestatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermitStatusRepository extends JpaRepository<Permitestatus, Integer> {
}
