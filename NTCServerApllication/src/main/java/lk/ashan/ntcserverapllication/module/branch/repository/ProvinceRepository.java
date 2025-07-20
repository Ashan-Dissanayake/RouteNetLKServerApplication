package lk.ashan.ntcserverapllication.module.branch.repository;

import lk.ashan.ntcserverapllication.module.branch.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {
}
