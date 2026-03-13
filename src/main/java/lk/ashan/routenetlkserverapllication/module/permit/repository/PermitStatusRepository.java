package lk.ashan.routenetlkserverapllication.module.permit.repository;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permitestatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PermitStatusRepository extends JpaRepository<Permitestatus, Integer> {
    Optional<Permitestatus> findByName(String active);
}
