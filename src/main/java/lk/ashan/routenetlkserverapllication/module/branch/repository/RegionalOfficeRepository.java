package lk.ashan.routenetlkserverapllication.module.branch.repository;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.RegionalOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionalOfficeRepository extends JpaRepository<RegionalOffice, Integer> {
}
