package lk.ashan.routenetlkserverapllication.module.crew.repository;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewStatusRepository extends JpaRepository<CrewStatus, Integer> {
}
