package lk.ashan.routenetlkserverapllication.module.crew.repository;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Crewstatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewStatusRepository extends JpaRepository<Crewstatus, Integer> {
}
