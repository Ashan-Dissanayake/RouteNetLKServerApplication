package lk.ashan.routenetlkserverapllication.module.employee.repository;

import lk.ashan.routenetlkserverapllication.module.employee.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {
}
