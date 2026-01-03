package lk.ashan.routenetlkserverapllication.module.roster.repository;

import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftstatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftStatusRepository extends JpaRepository<Shiftstatus, Integer> {
}
