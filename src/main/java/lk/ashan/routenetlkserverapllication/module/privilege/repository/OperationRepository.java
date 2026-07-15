package lk.ashan.routenetlkserverapllication.module.privilege.repository;

import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {
}
