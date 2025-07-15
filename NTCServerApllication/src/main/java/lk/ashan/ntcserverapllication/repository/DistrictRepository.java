package lk.ashan.ntcserverapllication.repository;

import lk.ashan.ntcserverapllication.model.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
}
