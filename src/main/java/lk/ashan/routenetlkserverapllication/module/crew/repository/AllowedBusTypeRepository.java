package lk.ashan.routenetlkserverapllication.module.crew.repository;

import lk.ashan.routenetlkserverapllication.module.crew.model.Bustype;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllowedBusTypeRepository extends JpaRepository<Bustype, Integer> {
}
