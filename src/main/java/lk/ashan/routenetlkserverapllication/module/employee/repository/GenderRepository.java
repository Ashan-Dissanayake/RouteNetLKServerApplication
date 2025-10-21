package lk.ashan.routenetlkserverapllication.module.employee.repository;

import lk.ashan.routenetlkserverapllication.module.employee.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {
}
