package lk.ashan.routenetlkserverapllication.module.grn.repository;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grnpart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrnPartRepository extends JpaRepository<Grnpart, Integer> {
}
