package lk.ashan.ntcserverapllication.module.branch.repository;

import lk.ashan.ntcserverapllication.module.branch.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
}
